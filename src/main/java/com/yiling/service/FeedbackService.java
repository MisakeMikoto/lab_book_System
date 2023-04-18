package com.yiling.service;

import com.yiling.domain.Feedback;

import java.util.List;

/**
 * @Author MisakiMikoto
 * @Date 2023/4/4 17:13
 */
public interface FeedbackService {
    List<Feedback> getFeedbacksByUserId(Integer userId);

    boolean add(Feedback feedback);




}
