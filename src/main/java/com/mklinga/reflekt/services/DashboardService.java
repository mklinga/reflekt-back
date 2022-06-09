package com.mklinga.reflekt.services;

import com.mklinga.reflekt.dtos.DashboardDto;
import com.mklinga.reflekt.model.User;
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
