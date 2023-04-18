package com.yiling.controller;

import com.yiling.domain.Feedback;
import com.yiling.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author MisakiMikoto
 * @Date 2023/4/4 17:28
 */
@RestController
@RequestMapping("/feedbacks")
public class FeedbackController {
    @Autowired
    private FeedbackService feedbackService;

    @GetMapping("/users/{userId}")
    public List<Feedback> getFeedbacksByUserId(@PathVariable("userId") Integer userId) {
        return feedbackService.getFeedbacksByUserId(userId);
    }
}
