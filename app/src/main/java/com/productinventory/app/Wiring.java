package com.productinventory.app;

import com.productinventory.persistence.PersistenceConfig;
import com.productinventory.usecase.UseCaseConfig;
import com.productinventory.web.WebConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({PersistenceConfig.class, WebConfig.class, UseCaseConfig.class})
public class Wiring {
}
