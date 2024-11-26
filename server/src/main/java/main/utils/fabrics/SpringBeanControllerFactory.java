package main.utils.fabrics;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ConfigurableApplicationContext;

public class SpringBeanControllerFactory {
    @Setter
    @Getter
    private static ConfigurableApplicationContext springContext;
}
