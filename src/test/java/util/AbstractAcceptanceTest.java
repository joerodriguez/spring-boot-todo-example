package util;

import com.github.joerodriguez.springbootexample.Application;
import org.fluentlenium.adapter.FluentTest;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Table;
import javax.persistence.metamodel.EntityType;
import javax.sql.DataSource;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.fluentlenium.core.filter.FilterConstructor.withText;


public abstract class AbstractAcceptanceTest extends FluentTest {

    public static final String PORT = "8081";

    private static ConfigurableApplicationContext context;

    @BeforeClass
    public static void start() throws Exception {
        final Future<ConfigurableApplicationContext> future = Executors
                .newSingleThreadExecutor().submit(
                        new Callable<ConfigurableApplicationContext>() {
                            @Override
                            public ConfigurableApplicationContext call() throws Exception {
                                return SpringApplication.run(Application.class, "--server.port=" + PORT);
                            }
                        });
        context = future.get(60, TimeUnit.SECONDS);
    }

    @AfterClass
    public static void stop() {
        if (context != null) {
            context.close();
        }
    }

    @After
    public void truncateTables() {
        DataSource dataSource = (DataSource) context.getBean("dataSource");
        EntityManager entityManager = ((EntityManagerFactory) context.getBean("entityManagerFactory")).createEntityManager();
        JdbcTemplate template = new JdbcTemplate(dataSource);

        for (EntityType<?> entity : entityManager.getMetamodel().getEntities()) {
            String tableName = entity.getBindableJavaType().getAnnotation(Table.class).name();
            template.execute("TRUNCATE " + tableName + " CASCADE;");
        }
    }

    protected static ApplicationContext getContext() {
        return context;
    }

    protected void navigate(String url) {
        goTo(getServerUrl() + url);
    }

    protected String getServerUrl() {
        final Environment environment = getContext().getEnvironment();
        final Integer port = environment.getProperty("server.port", Integer.class);
        final String contextPath = environment.getProperty("server.contextPath", String.class, "");

        return "http://localhost:" + PORT + contextPath;
    }

    protected void clickLink(String linkText) {
        findFirst("a", withText(linkText)).click();
    }

    protected void javascriptConfirm() {
        // can't click confirm dialog, so override it
        executeScript("window.confirm=function(){ return true; };");
    }
}
