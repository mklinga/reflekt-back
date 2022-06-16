package com.mklinga.reflekt.analytics.services;

import com.mklinga.reflekt.analytics.dtos.DashboardDto;
import com.mklinga.reflekt.authentication.model.User;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {
  public DashboardDto getDashboardData(User user) {
    DashboardDto data = new DashboardDto();
    data.setEntryCount(16);
    data.setWordCount(487L);
    return data;
  }
}
