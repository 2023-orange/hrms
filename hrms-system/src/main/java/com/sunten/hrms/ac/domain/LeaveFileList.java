package com.sunten.hrms.ac.domain;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.web.multipart.MultipartFile;

@Data
@ToString(callSuper = true)
@Accessors(chain = true)
public class LeaveFileList {
    private MultipartFile file;
    private String file_name;
    private String file_size;
    private String upload_time;
}
