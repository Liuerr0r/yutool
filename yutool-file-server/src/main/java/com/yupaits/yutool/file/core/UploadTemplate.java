package com.yupaits.yutool.file.core;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.upload.FastImageFile;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.yupaits.yutool.commons.result.Result;
import com.yupaits.yutool.commons.result.ResultWrapper;
import com.yupaits.yutool.file.exception.UploadException;
import com.yupaits.yutool.file.support.UploadProps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * 文件上传工具类
 * @author yupaits
 * @date 2019/7/22
 */
@Slf4j
public class UploadTemplate {

    private final FastFileStorageClient storageClient;

    public UploadTemplate(FastFileStorageClient storageClient) {
        this.storageClient = storageClient;
    }

    /**
     * 上传单个文件，返回文件的访问链接
     * @param file 待上传文件
     * @param uploadProps UploadProps参数
     * @return 文件访问链接
     */
    public String upload(MultipartFile file, UploadProps uploadProps) throws UploadException {
        checkUploadProps(uploadProps);
        try (InputStream fileInputStream = file.getInputStream()) {
            StorePath filePath = storageClient.uploadFile(fileInputStream, file.getSize(), FilenameUtils.getExtension(file.getOriginalFilename()), null);
            return uploadProps.getStoreGroup() + "/" + filePath.getFullPath();
        } catch (IOException e) {
            throw new UploadException("文件上传失败", e);
        }
    }

    /**
     * 批量上传文件，返回文件访问链接列表
     * @param files 待上传文件列表
     * @param uploadProps UploadProps参数
     * @return 文件访问链接列表
     */
    public Map<String, String> uploadBatch(List<MultipartFile> files, UploadProps uploadProps) throws UploadException {
        checkUploadProps(uploadProps);
        if (CollectionUtils.isEmpty(files)) {
            throw new UploadException("上传文件列表为空");
        }
        Map<String, String> filePaths = Maps.newTreeMap();
        batchUpload(files, uploadProps, filePaths);
        return filePaths;
    }

    /**
     * 上传单个图片，返回图片的访问链接
     * @param image 图片文件
     * @param uploadProps UploadProps参数
     * @return 图片访问链接
     */
    public String uploadImage(MultipartFile image, UploadProps uploadProps) throws UploadException {
        checkUploadProps(uploadProps);
        try (InputStream imageInputStream = image.getInputStream()) {
            FastImageFile.Builder imageBuilder = new FastImageFile.Builder();
            imageBuilder.withFile(imageInputStream, image.getSize(), FilenameUtils.getExtension(image.getOriginalFilename()));
            if (uploadProps.isThumb()) {
                if (uploadProps.getWidth() > 0 && uploadProps.getHeight() > 0) {
                    imageBuilder.withThumbImage(uploadProps.getWidth(), uploadProps.getHeight());
                } else if (uploadProps.getPercent() > 0.0) {
                    imageBuilder.withThumbImage(uploadProps.getPercent());
                }
            }
            StorePath imagePath = storageClient.uploadImage(imageBuilder.build());
            return uploadProps.getStoreGroup() + "/" + imagePath.getFullPath();
        } catch (IOException e) {
            throw new UploadException("图片上传失败", e);
        }
    }

    /**
     * 批量上传图片，返回图片访问链接列表
     * @param images 图片文件列表
     * @param uploadProps UploadProps参数
     * @return 图片访问链接列表
     */
    public Map<String, String> uploadBatchImage(List<MultipartFile> images, UploadProps uploadProps) throws UploadException {
        checkUploadProps(uploadProps);
        if (CollectionUtils.isEmpty(images)) {
            throw new UploadException("上传图片列表为空");
        }
        Map<String, String> imagePaths = Maps.newTreeMap();
        batchUploadImage(images, uploadProps, imagePaths);
        return imagePaths;
    }

    /**
     * 上传单个文件，返回文件的访问链接
     * @param file 待上传文件
     * @param uploadProps UploadProps参数
     * @return 文件访问链接
     */
    public Result<String> resultUpload(MultipartFile file, UploadProps uploadProps) throws UploadException {
        return ResultWrapper.success(upload(file, uploadProps));
    }

    /**
     * 批量上传文件，返回文件访问链接列表
     * @param files 待上传文件列表
     * @param uploadProps UploadProps参数
     * @return 文件访问链接Map
     */
    public Result<Map<String, String>> resultUploadBatch(List<MultipartFile> files, UploadProps uploadProps) throws UploadException {
        return ResultWrapper.success(uploadBatch(files, uploadProps));
    }

    /**
     * 上传单个图片，返回图片的访问链接
     * @param image 图片文件
     * @param uploadProps UploadProps参数
     * @return 图片访问链接
     */
    public Result<String> resultUploadImage(MultipartFile image, UploadProps uploadProps) throws UploadException {
        return ResultWrapper.success(uploadImage(image, uploadProps));
    }

    /**
     * 批量上传图片，返回图片访问链接列表
     * @param images 图片文件列表
     * @param uploadProps UploadProps参数
     * @return 图片访问链接Map
     */
    public Result<Map<String, String>> resultUploadBatchImage(List<MultipartFile> images, UploadProps uploadProps) throws UploadException {
        return ResultWrapper.success(uploadBatchImage(images, uploadProps));
    }

    /**
     * 校验UploadProps参数
     * @param uploadProps UploadProps参数
     * @throws UploadException 抛出UploadException
     */
    private void checkUploadProps(UploadProps uploadProps) throws UploadException {
        if (uploadProps == null || !uploadProps.isValid()) {
            throw new UploadException(String.format("上传配置校验失败，配置信息：%s", uploadProps));
        }
    }

    /**
     * 批量上传处理
     * @param files 待上传文件列表
     * @param uploadProps UploadProps参数
     * @param paths 文件访问链接Map
     */
    private void batchUpload(List<MultipartFile> files, UploadProps uploadProps, Map<String, String> paths) {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("upload-file-worker-%d").build();
        ExecutorService pool = new ThreadPoolExecutor(5, 200,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());
        for (MultipartFile file : files) {
            Future<String> future = pool.submit(new UploadCallable(file, uploadProps));
            try {
                paths.put(file.getOriginalFilename(), future.get());
            } catch (InterruptedException | ExecutionException e) {
                log.error(e.getMessage(), e);
            }
        }
        pool.shutdown();
    }

    /**
     * 批量上传图片处理
     * @param images 待上传图片列表
     * @param uploadProps UploadProps参数
     * @param paths 图片访问链接Map
     */
    private void batchUploadImage(List<MultipartFile> images, UploadProps uploadProps, Map<String, String> paths) {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("upload-image-worker-%d").build();
        ExecutorService pool = new ThreadPoolExecutor(5, 200,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());
        for (MultipartFile image : images) {
            Future<String> future = pool.submit(new UploadImageCallable(image, uploadProps));
            try {
                paths.put(image.getOriginalFilename(), future.get());
            } catch (InterruptedException | ExecutionException e) {
                log.error(e.getMessage(), e);
            }
        }
        pool.shutdown();
    }

    /**
     * 上传任务Callable
     */
    class UploadCallable implements Callable<String> {

        private final MultipartFile file;
        private final UploadProps uploadProps;

        UploadCallable(MultipartFile file, UploadProps uploadProps) {
            this.file = file;
            this.uploadProps = uploadProps;
        }

        @Override
        public String call() {
            try {
                return upload(file, uploadProps);
            } catch (UploadException e) {
                log.error(e.getMessage(), e);
                return null;
            }
        }
    }

    /**
     * 上传图片任务Callable
     */
    class UploadImageCallable implements Callable<String> {

        private final MultipartFile image;
        private final UploadProps uploadProps;

        UploadImageCallable(MultipartFile image, UploadProps uploadProps) {
            this.image = image;
            this.uploadProps = uploadProps;
        }

        @Override
        public String call() {
            try {
                return uploadImage(image, uploadProps);
            } catch (UploadException e) {
                log.error(e.getMessage(), e);
                return null;
            }
        }
    }
}
