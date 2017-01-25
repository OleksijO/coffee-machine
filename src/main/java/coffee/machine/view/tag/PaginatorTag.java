package coffee.machine.view.tag;

import org.apache.log4j.Logger;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;
import java.io.IOException;

/**
 * This class represent custom tag handler, which transforms tag to list page navigation.
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class PaginatorTag implements Tag {
    private static final int FIRST_PAGE_NUMBER = 1;

    protected PageContext pageContext;
    /**
     * Text of first page href
     */
    private String labelFirst;
    /**
     * Text of previous page href
     */
    private String labelPrevious;
    /**
     * Text of next page href
     */
    private String labelNext;
    /**
     * Text of last page href
     */
    private String labelLast;
    /**
     * Current page number value
     */
    private int currentPageNumber;
    /**
     * Last page number value
     */
    private int lastPageNumber;

    /**
     * Value of 'page' parameter
     */
    private String parameterPage;

    @Override
    public int doStartTag() throws JspException {
        if ((currentPageNumber == lastPageNumber) && (currentPageNumber == FIRST_PAGE_NUMBER)) {
            return Tag.SKIP_BODY;
        }
        try {
            JspWriter out = pageContext.getOut();
            printDelimiter(out);
            if (currentPageNumber > FIRST_PAGE_NUMBER) {
                printReferencesForFirstAndPrevious(out);
            } else {
                printNonReferencesFirstAndPrevious(out);
            }
            printCurrentPage(out);
            if (currentPageNumber < lastPageNumber) {
                printReferencesForNextAndLast(out);
            } else {
                printNonReferencesNextAndLast(out);
            }
            printDelimiter(out);
        } catch (Exception e) {
            Logger.getLogger(PaginatorTag.class).error(e);
            throw new JspException(e);
        }
        return Tag.SKIP_BODY;
    }



    private void printDelimiter(JspWriter out) throws IOException {
        out.println("<hr width=\"50%\">\n");
    }

    private void printReferencesForFirstAndPrevious(JspWriter out) throws IOException {
        out.println("" +
                "            <a href=\"?" + parameterPage + "=" + FIRST_PAGE_NUMBER + "\">\n" +
                "                " + labelFirst + "\n" +
                "            </a>&nbsp;\n" +
                "            <a href=\"?" + parameterPage + "=" + (currentPageNumber - 1) + "\">\n" +
                "                " + labelPrevious + "\n" +
                "            </a>&nbsp;" +
                "");
    }

    private void printNonReferencesFirstAndPrevious(JspWriter out) throws IOException {
        out.println("" +
                "        " + labelFirst + "&nbsp;\n" +
                "        " + labelPrevious + "&nbsp;\n"+
                "");
    }

    private void printCurrentPage(JspWriter out) throws IOException {
        out.println("[ " + currentPageNumber + " ]&nbsp;" );
    }

    private void printReferencesForNextAndLast(JspWriter out) throws IOException {
        out.println("" +
                " <a href=\"?" + parameterPage + "=" + (currentPageNumber + 1) + "\">\n" +
                "                " + labelNext + "\n" +
                "            </a>&nbsp;\n" +
                "            <a href=\"?" + parameterPage + "=" + lastPageNumber + "\">\n" +
                "                " + labelLast + "\n" +
                "            </a>" +
                "");
    }

    private void printNonReferencesNextAndLast(JspWriter out) throws IOException {
        out.println("" +
                "        " + labelNext + "&nbsp;\n" +
                "        " + labelLast +
                "");
    }

    @Override
    public int doEndTag() throws JspException {
        return Tag.EVAL_PAGE;
    }

    @Override
    public void release() {
    }

    @Override
    public void setPageContext(PageContext pageContext) {
        this.pageContext = pageContext;
    }

    @Override
    public void setParent(Tag tag) {
    }

    @Override
    public Tag getParent() {
        return null;
    }

    public void setLabelFirst(String labelFirst) {
        this.labelFirst = labelFirst;
    }

    public void setLabelPrevious(String labelPrevious) {
        this.labelPrevious = labelPrevious;
    }

    public void setLabelNext(String labelNext) {
        this.labelNext = labelNext;
    }

    public void setLabelLast(String labelLast) {
        this.labelLast = labelLast;
    }

    public void setCurrentPageNumber(String currentPageNumber) throws JspException {
        try {
            this.currentPageNumber = Integer.parseInt(currentPageNumber);
        } catch (NumberFormatException e) {
            Logger.getLogger(PaginatorTag.class).error(e);
            throw new JspException(e);
        }
    }

    public void setLastPageNumber(String lastPageNumber) throws JspException {
        try {
            this.lastPageNumber = Integer.parseInt(lastPageNumber);
        } catch (NumberFormatException e) {
            Logger.getLogger(PaginatorTag.class).error(e);
            throw new JspException(e);
        }
    }

    public void setParameterPage(String parameterPage) {
        this.parameterPage = parameterPage;
    }
}
