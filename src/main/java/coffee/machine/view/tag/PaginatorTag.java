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
    private static final int FIRST = 1;

    protected PageContext pageContext;
    /**
     * Text of first page href
     */
    private String first;
    /**
     * Text of previous page href
     */
    private String previous;
    /**
     * Text of next page href
     */
    private String next;
    /**
     * Text of last page href
     */
    private String last;
    /**
     * Current page number value
     */
    private int currentPage;
    /**
     * Last page number value
     */
    private int lastPage;

    /**
     * Value of 'page' parameter
     */
    private String pageParameter;

    @Override
    public int doStartTag() throws JspException {
        if ((currentPage == lastPage) && (currentPage == FIRST)) {
            return Tag.SKIP_BODY;
        }
        try {
            JspWriter out = pageContext.getOut();
            printDelimiter(out);
            if (currentPage > FIRST) {
                printReferencesForFirstAndPrevious(out);
            } else {
                printNonReferencesFirstAndPrevious(out);
            }
            if (currentPage < lastPage) {
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
                "            <a href=\"?" + pageParameter + "=" + FIRST + "\">\n" +
                "                " + first + "\n" +
                "            </a>&nbsp;\n" +
                "            <a href=\"?" + pageParameter + "=" + (currentPage - 1) + "\">\n" +
                "                " + previous + "\n" +
                "            </a>&nbsp;" +
                "");
    }

    private void printNonReferencesFirstAndPrevious(JspWriter out) throws IOException {
        out.println("" +
                "        " + first + "&nbsp;\n" +
                "        " + previous + "&nbsp;\n"+
                "");
    }

    private void printReferencesForNextAndLast(JspWriter out) throws IOException {
        out.println("" +
                " <a href=\"?" + pageParameter + "=" + (currentPage + 1) + "\">\n" +
                "                " + next + "\n" +
                "            </a>&nbsp;\n" +
                "            <a href=\"?" + pageParameter + "=" + lastPage + "\">\n" +
                "                " + last + "\n" +
                "            </a>" +
                "");
    }

    private void printNonReferencesNextAndLast(JspWriter out) throws IOException {
        out.println("" +
                "        " + next + "&nbsp;\n" +
                "        " + last +
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

    public void setFirst(String first) {
        this.first = first;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public void setCurrentPage(String currentPage) throws JspException {
        try {
            this.currentPage = Integer.parseInt(currentPage);
        } catch (NumberFormatException e) {
            Logger.getLogger(PaginatorTag.class).error(e);
            throw new JspException(e);
        }
    }

    public void setLastPage(String lastPage) throws JspException {
        try {
            this.lastPage = Integer.parseInt(lastPage);
        } catch (NumberFormatException e) {
            Logger.getLogger(PaginatorTag.class).error(e);
            throw new JspException(e);
        }
    }

    public void setPageParameter(String pageParameter) {
        this.pageParameter = pageParameter;
    }
}
