package coffee_machine.view.tag;

import coffee_machine.CoffeeMachineConfig;
import coffee_machine.view.Attributes;
import org.apache.log4j.Logger;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created by oleksij.onysymchuk@gmail on 27.11.2016.
 */
public class LoginFormTag implements Tag {
    private PageContext pageContext;
    private String action;
    private String cssClass;
    private String formTitleMessageKey;
    private String loginLabelMessageKey;
    private String parameterLogin;
    private String loginPreviosValue;
    private String passwordLabelMessageKey;
    private String parameterPassword;
    private String submitMessageKey;


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
                    "                <td><br><label for=\"login\">" + bundle.getString(loginLabelMessageKey) + "</label>&nbsp;<br><br></td>\n" +
                    "                <td><br><input id=\"login\" type=\"text\" name=\"" + parameterLogin + "\"\n" +
                    "                               value=\"" + loginPreviosValue + "\"/><br><br></td>\n" +
                    "            </tr>\n" +
                    "            <tr>\n" +
                    "                <td><br><label for=\"pswd\">" + bundle.getString(passwordLabelMessageKey) + "</label>&nbsp;<br><br></td>\n" +
                    "                <td><br><input id=\"pswd\" type=\"password\" name=\"" + parameterPassword + "\"/><br><br></td>\n" +
                    "            </tr>\n" +
                    "            <tr>\n" +
                    "                <td colspan=\"2\">\n" +
                    "                    <div align=\"center\"><input type=\"submit\" value=\"" + bundle.getString(submitMessageKey) + "\"></div>\n" +
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

    public void setLoginPreviosValue(String loginPreviosValue) {
        this.loginPreviosValue = loginPreviosValue;
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

    @Override
    public String toString() {
        return "LoginFormTag{" +
                "pageContext=" + pageContext +
                ", action='" + action + '\'' +
                ", cssClass='" + cssClass + '\'' +
                ", formTitleMessageKey='" + formTitleMessageKey + '\'' +
                ", loginLabelMessageKey='" + loginLabelMessageKey + '\'' +
                ", parameterLogin='" + parameterLogin + '\'' +
                ", loginPreviosValue='" + loginPreviosValue + '\'' +
                ", passwordLabelMessageKey='" + passwordLabelMessageKey + '\'' +
                ", parameterPassword='" + parameterPassword + '\'' +
                ", submitMessageKey='" + submitMessageKey + '\'' +
                '}';
    }
}
