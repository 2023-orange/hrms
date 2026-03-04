package com.sunten.hrms.ac.domain;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author Zouyp
 */
@Data
@ToString(callSuper = true)
@Accessors(chain = true)
public class UpdateReviewFormData {
    private List<ReviewFormData> updateReviewFormData;
}
