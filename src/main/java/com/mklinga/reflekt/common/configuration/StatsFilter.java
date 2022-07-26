package com.mklinga.reflekt.common.configuration;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * StatsFilter's only purpose is to log endpoints that take a long time (see if-clause at line 41)
 */
@Component
@WebFilter("/*")
public class StatsFilter implements Filter {

  private static final Logger logger = LoggerFactory.getLogger(StatsFilter.class);

  @Override
  public void init(FilterConfig filterConfig) {
  }

  @Override
  public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
      throws IOException, ServletException {
    Instant start = Instant.now();
    try {
      chain.doFilter(req, resp);
    } finally {
      Instant finish = Instant.now();
      long time = Duration.between(start, finish).toMillis();

      if (time > 250) {
        logger.info("Request for the endpoint {} took {} ms!",
            ((HttpServletRequest) req).getRequestURI(), time);
      }
    }
  }

  @Override
  public void destroy() {
  }
}
