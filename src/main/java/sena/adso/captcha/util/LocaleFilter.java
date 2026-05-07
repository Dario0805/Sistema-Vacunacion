package sena.adso.captcha.util;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.Locale;

public class LocaleFilter implements Filter {

    private static final String LANG_PARAM = "lang";
    private static final String LOCALE_SESSION_KEY = "locale";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession();

        String lang = request.getParameter(LANG_PARAM);

        if (lang != null && !lang.isEmpty()) {
            Locale locale = getLocaleFromString(lang);
            session.setAttribute(LOCALE_SESSION_KEY, locale);
        }

        if (session.getAttribute(LOCALE_SESSION_KEY) == null) {
            session.setAttribute(LOCALE_SESSION_KEY, new Locale("es"));
        }

        Locale currentLocale = (Locale) session.getAttribute(LOCALE_SESSION_KEY);

        request.setAttribute("locale", currentLocale);
        response.setLocale(currentLocale);
        httpResponse.setCharacterEncoding("UTF-8");

        chain.doFilter(request, response);
    }

   private Locale getLocaleFromString(String lang) {
    switch (lang.toLowerCase()) {
        case "en":
            return new Locale("en");
        case "it":
            return new Locale("it");
        case "fr":
            return new Locale("fr");
        case "es":
        default:
            return new Locale("es");
    }
}
    @Override
    public void destroy() {
    }
}