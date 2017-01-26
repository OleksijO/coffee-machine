package coffee.machine.view.tag;

import coffee.machine.config.CoffeeMachineConfig;
import org.apache.log4j.Logger;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;
import java.util.Locale;
import java.util.ResourceBundle;

import static coffee.machine.controller.RegExp.REGEXP_FULL_NAME;
import static coffee.machine.view.config.Attributes.USER_LOCALE;

/**
 * This class represent custom tag handler, which transforms tag to register form in HTML in user current locale.
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class RegisterFormTag extends LoginFormTag {
    private static final Logger logger = Logger.getLogger(RegisterFormTag.class);
    /**
     * label text for full name field label - should be message key from resource bundle
     */
    private String fullNameLabelMessageKey;
    /**
     * Full name input name parameter
     */
    private String parameterFullName;
    /**
     * previous value of login - in case of login error should stay in input field
     */
    private String fullNamePreviousValue;

    @Override
    public int doStartTag() throws JspException {
        try {
            Locale locale = (Locale) pageContext.getSession().getAttribute(USER_LOCALE);
            ResourceBundle bundle = ResourceBundle.getBundle(CoffeeMachineConfig.MESSAGES, locale);
            pageContext.getOut().println("" +
                    formHeader(bundle) +
                    emailInputField(bundle) +
                    passwordInputField(bundle) +
                    fullNameInputField(bundle) +
                    submitButton(bundle) +
                    formFooter());
        } catch (Exception e) {
            logger.error(e);
            throw new JspException(e);
        }
        return Tag.SKIP_BODY;
    }

    private String fullNameInputField(ResourceBundle bundle){
        return "            <tr>\n" +
                "                <td><br><label for=\"fullName\">" + bundle.getString(fullNameLabelMessageKey)
                + "</label>&nbsp;<br><br></td>\n" +
                "                <td><br><input id=\"fullName\" type=\"text\" name=\"" + parameterFullName + "\"\n" +
                "                               value=\"" + fullNamePreviousValue + "\"" +
                "                  required     pattern=\"" + REGEXP_FULL_NAME + "\""+
                "  title=\""+bundle.getString("error.register.full.name.do.not.match.pattern")+ "\"/><br><br></td>\n" +
                "            </tr>\n";
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

    public void setFullNameLabelMessageKey(String fullNameLabelMessageKey) {
        this.fullNameLabelMessageKey = fullNameLabelMessageKey;
    }

    public void setParameterFullName(String parameterFullName) {
        this.parameterFullName = parameterFullName;
    }

    public void setFullNamePreviousValue(String fullNamePreviousValue) {
        this.fullNamePreviousValue = fullNamePreviousValue;
    }
}
