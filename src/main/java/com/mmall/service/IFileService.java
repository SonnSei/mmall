package com.mmall.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @Classname IFileService
 * @Description TODO
 * @Date 2019/12/29 19:01
 * @Author SonnSei
 */
public interface IFileService {
    String upload(MultipartFile file, String path);
}
