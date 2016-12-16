package coffee.machine.view.tag;

import coffee.machine.config.CoffeeMachineConfig;
import coffee.machine.controller.RegExp;
import coffee.machine.view.Attributes;
import org.apache.log4j.Logger;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * This class represent custom tag handler, which transforms tag to login form in HTML in user current locale.
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class LoginFormTag implements Tag {
    private PageContext pageContext;
    /**
     * form action path
     */
    private String action;
    /**
     * css style class
     */
    private String cssClass;
    /**
     * label to be shown above input fields - should be message key from resource bundle
     */
    private String formTitleMessageKey;
    /**
     * label text for login field label
     */
    private String loginLabelMessageKey;
    /**
     * login input name attribute - should be message key from resource bundle
     */
    private String parameterLogin;
    /**
     * previous value of login - in case of login error should stay in input field
     */
    private String loginPreviousValue;
    /**
     * label text for password field label - should be message key from resource bundle
     */
    private String passwordLabelMessageKey;
    /**
     * password input name attribute
     */
    private String parameterPassword;
    /**
     * submit button text - should be message key from resource bundle
     */
    private String submitMessageKey;
    /**
     * cancel button text - should be message key from resource bundle
     */
    private String cancelMessageKey;


    @Override
    public int doStartTag() throws JspException {
        try {
            Locale locale = (Locale) pageContext.getSession().getAttribute(Attributes.USER_LOCALE);
            ResourceBundle bundle = ResourceBundle.getBundle(CoffeeMachineConfig.MESSAGES, locale);
            pageContext.getOut().println("" +
                    "<br>" +
                    "<div class=\"" + cssClass + "\">\n" +
                    "    <br>\n" +
                    "    <b>" + bundle.getString(formTitleMessageKey) + "</b><br>\n" +
                    "    <hr>\n" +
                    "    <form action=\"" + action + "\" method=\"post\">\n" +
                    "        <table>\n" +
                    "            <tr>\n" +
                    "                <td><br><label for=\"login\">" + bundle.getString(loginLabelMessageKey)
                                                                    + "</label>&nbsp;<br><br></td>\n" +
                    "                <td><br><input id=\"login\" type=\"text\" name=\"" + parameterLogin + "\"\n" +
                    "                               value=\"" + loginPreviousValue + "\"" +
                    "                  required     pattern=\"" + RegExp.REGEXP_EMAIL + "\""+
                    "  title=\""+bundle.getString("error.login.email.do.not.match.pattern")+ "\"/><br><br></td>\n" +
                    "            </tr>\n" +
                    "            <tr>\n" +
                    "                <td><br><label for=\"pswd\">" + bundle.getString(passwordLabelMessageKey)
                                                                    + "</label>&nbsp;<br><br></td>\n" +
                    "                <td><br><input id=\"pswd\" type=\"password\" name=\"" + parameterPassword
                                                                                            + "\""+
                    "                       required min=4 max=12 pattern=\"" + RegExp.REGEXP_PASSWORD + "\""+
                    "  title=\""+bundle.getString("error.login.password.do.not.match.pattern")+ "\"/><br><br></td>\n" +
                    "            </tr>\n" +
                    "            <tr>\n" +
                    "                <td colspan=\"2\">\n" +
                    "                   <div align=\"center\">" +
                    "                         <input type=\"submit\" value=\""
                                                            + bundle.getString(submitMessageKey) + "\">" +
                    "                         &nbsp;\n" +
                    "                        <input type=\"button\" value=\""
                                                    + bundle.getString(cancelMessageKey)
                                                    + "\" onclick=\"history.back()\">" +
                    "                   </div>\n" +
                    "                </td>\n" +
                    "            </tr>\n" +
                    "        </table>\n" +
                    "    </form>\n" +
                    "\n" +
                    "    <br>\n" +
                    "</div>" +
                    "");
        } catch (Exception e) {
            Logger.getLogger(LoginFormTag.class).error(e);
            throw new JspException(e);
        }
        return Tag.SKIP_BODY;
    }

    @Override
    public int doEndTag() throws JspException {
        return Tag.EVAL_PAGE;
    }

    @Override
    public void setPageContext(PageContext pageContext) {
        this.pageContext = pageContext;
    }

    @Override
    public void release() {
    }

    @Override
    public void setParent(Tag tag) {
    }

    @Override
    public Tag getParent() {
        return null;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setCssClass(String cssClass) {
        this.cssClass = cssClass;
    }

    public void setFormTitleMessageKey(String formTitleMessageKey) {
        this.formTitleMessageKey = formTitleMessageKey;
    }

    public void setLoginLabelMessageKey(String loginLabelMessageKey) {
        this.loginLabelMessageKey = loginLabelMessageKey;
    }

    public void setParameterLogin(String parameterLogin) {
        this.parameterLogin = parameterLogin;
    }

    public void setLoginPreviousValue(String loginPreviousValue) {
        this.loginPreviousValue = loginPreviousValue;
    }

    public void setPasswordLabelMessageKey(String passwordLabelMessageKey) {
        this.passwordLabelMessageKey = passwordLabelMessageKey;
    }

    public void setParameterPassword(String parameterPassword) {
        this.parameterPassword = parameterPassword;
    }

    public void setSubmitMessageKey(String submitMessageKey) {
        this.submitMessageKey = submitMessageKey;
    }

    public void setCancelMessageKey(String cancelMessageKey) {
        this.cancelMessageKey = cancelMessageKey;
    }
}
