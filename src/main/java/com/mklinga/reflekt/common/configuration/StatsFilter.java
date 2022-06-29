package com.mklinga.reflekt.common.configuration;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@WebFilter("/*")
public class StatsFilter implements Filter {

  private static final Logger logger = LoggerFactory.getLogger(StatsFilter.class);

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {}

  @Override
  public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
    Instant start = Instant.now();
    try {
      chain.doFilter(req, resp);
    } finally {
      Instant finish = Instant.now();
      long time = Duration.between(start, finish).toMillis();

      /* Log the slow endpoints */
      if (time > 250) {
        logger.info("Request for the endpoint {} took {} ms!", ((HttpServletRequest) req).getRequestURI(), time);
      }
    }
  }

  @Override
  public void destroy() {}
}
