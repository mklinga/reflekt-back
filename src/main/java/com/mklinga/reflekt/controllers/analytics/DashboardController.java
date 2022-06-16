package com.mklinga.reflekt.controllers.analytics;

import com.mklinga.reflekt.dtos.DashboardDto;
import com.mklinga.reflekt.model.UserPrincipal;
import com.mklinga.reflekt.services.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

  private DashboardService dashboardService;

  @Autowired
  public DashboardController(DashboardService dashboardService) {
    this.dashboardService = dashboardService;
  }

  @GetMapping("")
  public ResponseEntity<DashboardDto> getDashboardData(
      @AuthenticationPrincipal UserPrincipal userPrincipal
  ) {

   return ResponseEntity.ok(dashboardService.getDashboardData(userPrincipal.getUser()));
  }
}
