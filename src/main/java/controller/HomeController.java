package controller;

import com.sun.org.apache.bcel.internal.util.ClassPath;
import domain.User;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.LazyContextVariable;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.messageresolver.StandardMessageResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

public class HomeController extends HttpServlet{
    private TemplateEngine templateEngine;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(config.getServletContext());
        templateResolver.setPrefix("/WEB-INF/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCacheTTLMs(3600000L);
        templateResolver.setCacheable(true);
        templateResolver.setCharacterEncoding("UTF-8");
        templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
        StandardMessageResolver messageResolver = new StandardMessageResolver();
        Properties p   = new Properties();
        FileInputStream fileInputStream = null;
        try {
            InputStream resourceAsStream = this.getClass().getResourceAsStream("/home.properties");
            p.load(resourceAsStream);
        } catch (Exception e){

        }

        messageResolver.setDefaultMessages(p);
        templateEngine.setMessageResolver(messageResolver);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");
        WebContext webContext = new WebContext(req, resp, req.getServletContext());

        webContext.setVariable("today",Calendar.getInstance());
        webContext.setVariable("name","hello \"李四\" good day");

        User user = new User("张三", 12);
        webContext.setVariable("user",user);

        webContext.setVariable("list", new LazyContextVariable<List<User>>() {
            @Override
            protected List<User> loadValue() {
                List<User> list = new ArrayList<>();
                list.add(new User("王五",11));
                list.add(new User("赵柳", 12));
                list.add(new User("王八河", 13));
                return list;
            }
        });


        templateEngine.process("home",webContext,resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.service(req, resp);
    }
}
