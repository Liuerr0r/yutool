package com.yupaits.yutool.file.core;

import com.github.tobato.fastdfs.domain.proto.storage.DownloadByteArray;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.google.common.net.HttpHeaders;
import com.yupaits.yutool.file.exception.DownloadException;
import com.yupaits.yutool.file.support.DownloadProps;
import com.yupaits.yutool.file.utils.FastdfsUtils;
import com.yupaits.yutool.file.utils.WatermarkUtils;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.filters.Watermark;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.MediaType;
import org.springframework.util.FileCopyUtils;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * 文件下载工具类
 * @author yupaits
 * @date 2019/7/22
 */
public class DownloadTemplate {

    private final FastFileStorageClient storageClient;

    public DownloadTemplate(FastFileStorageClient storageClient) {
        this.storageClient = storageClient;
    }

    /**
     * 下载文件
     * @param response Response响应体
     * @param fullPath fastdfs文件路径
     * @throws IOException 抛出IOException
     */
    public void downloadFile(HttpServletResponse response, String fullPath, DownloadProps downloadProps) throws IOException, DownloadException {
        checkDownloadProps(downloadProps);
        byte[] fileBytes = storageClient.downloadFile(FastdfsUtils.getGroupName(fullPath), FastdfsUtils.getPath(fullPath), new DownloadByteArray());
        if (downloadProps.isThumb() || downloadProps.isWithWatermark()) {
            InputStream inputStream = new ByteArrayInputStream(fileBytes);
            Thumbnails.Builder builder = Thumbnails.of(inputStream);
            if (downloadProps.isThumb()) {
                if (downloadProps.getWidth() > 0 && downloadProps.getHeight() > 0) {
                    builder.size(downloadProps.getWidth(), downloadProps.getHeight());
                } else {
                    builder.scale(downloadProps.getScale());
                }
                builder.outputQuality(downloadProps.getQuality());
            }
            if (downloadProps.isWithWatermark()) {
                BufferedImage bufferedImage;
                switch (downloadProps.getWatermarkType()) {
                    case TEXT:
                        bufferedImage = WatermarkUtils.textToBufferedImage(downloadProps.getWatermarkText());
                        break;
                    case PICTURE:
                        bufferedImage = ImageIO.read(new File(downloadProps.getWatermarkPic()));
                        break;
                    default:
                        throw new DownloadException(String.format("不支持的图片水印类型，参数：%s", downloadProps.getWatermarkType()));
                }
                Watermark watermark = new Watermark(downloadProps.getWatermarkPos(), bufferedImage, downloadProps.getWatermarkOpacity());
                builder.watermark(watermark);
                if (!downloadProps.isThumb()) {
                    builder.scale(1.0);
                }
            }
            sendFile(response, builder, FilenameUtils.getName(fullPath));
        } else {
            sendFile(response, fileBytes, FilenameUtils.getName(fullPath));
        }
    }

    /**
     * 发送文件
     * @param response Response响应体
     * @param fileBytes 文件字节数组
     * @param filename 文件名
     */
    private void sendFile(HttpServletResponse response, byte[] fileBytes, String filename) throws IOException {
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        response.setContentLengthLong(fileBytes.length);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" +
                URLEncoder.encode(filename, StandardCharsets.UTF_8.name())+ "\"");
        FileCopyUtils.copy(fileBytes, response.getOutputStream());
    }

    /**
     * 发送文件
     * @param response Response响应体
     * @param builder ThumbnailBuilder
     * @param filename 文件名
     */
    private void sendFile(HttpServletResponse response, Thumbnails.Builder builder, String filename) throws IOException {
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" +
                URLEncoder.encode(filename, StandardCharsets.UTF_8.name())+ "\"");
        builder.toOutputStream(response.getOutputStream());
    }

    /**
     * 校验DownloadProps参数
     * @param downloadProps DownloadProps参数
     */
    private void checkDownloadProps(DownloadProps downloadProps) throws DownloadException {
        if (downloadProps == null || !downloadProps.isValid()) {
            throw new DownloadException(String.format("下载配置校验失败，配置信息：%s", downloadProps));
        }
    }
}
