package com.yiling.service.impl;

import com.yiling.dao.FeedbackDao;
import com.yiling.domain.Feedback;
import com.yiling.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author MisakiMikoto
 * @Date 2023/4/4 17:14
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class FeedbackServiceImpl implements FeedbackService {
    @Autowired
    private FeedbackDao feedbackDao;

    @Override
    public List<Feedback> getFeedbacksByUserId(Integer userId) {
        return feedbackDao.selectByUserId(userId);
    }

    @Override
    public boolean add(Feedback feedback) {
        int insert = feedbackDao.insert(feedback);
        if(insert == 1){
            return true;
        }
        return false;
    }
}
