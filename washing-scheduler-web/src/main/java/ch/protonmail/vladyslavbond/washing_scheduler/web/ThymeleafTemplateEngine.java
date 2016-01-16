package ch.protonmail.vladyslavbond.washing_scheduler.web;

import nz.net.ultraq.thymeleaf.LayoutDialect;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

enum ThymeleafTemplateEngine
{
    INSTANCE;
    
    private final TemplateEngine templateEngine;
    
    private ThymeleafTemplateEngine ( ) 
    {
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver();
        templateResolver.setTemplateMode("HTML5");
        templateResolver.setCharacterEncoding("UTF-8");
        templateResolver.setPrefix("/WEB-INF/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setCacheTTLMs(3600000L);
        
        templateEngine = new TemplateEngine();
        templateEngine.addDialect(new LayoutDialect ( ));
        templateEngine.setTemplateResolver(templateResolver);
    }
    
    public static TemplateEngine getInstance ( )
    {
        return INSTANCE.templateEngine;
    }
}
