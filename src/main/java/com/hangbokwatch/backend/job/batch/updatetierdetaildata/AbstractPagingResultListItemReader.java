package com.hangbokwatch.backend.job.batch.updatetierdetaildata;

import com.hangbokwatch.backend.domain.player.Player;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.item.database.AbstractPagingItemReader;
import org.springframework.batch.item.support.AbstractItemCountingItemStreamItemReader;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.util.List;

public abstract class AbstractPagingResultListItemReader<T> extends AbstractItemCountingItemStreamItemReader<T>
        implements InitializingBean{

    protected Log logger = LogFactory.getLog(getClass());

    private volatile boolean initialized = false;

    private int pageSize = 10;

    private volatile int current = 0;

    private volatile int page = 0;

    protected volatile List<Player> results;

    private Object lock = new Object();

    public AbstractPagingResultListItemReader() {
        setName(ClassUtils.getShortName(AbstractPagingResultListItemReader.class));
    }

    /**
     * The current page number.
     * @return the current page
     */
    public int getPage() {
        return page;
    }

    /**
     * The page size configured for this reader.
     * @return the page size
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * The number of rows to retrieve at a time.
     *
     * @param pageSize the number of rows to fetch per page
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * Check mandatory properties.
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.isTrue(pageSize > 0, "pageSize must be greater than zero");
    }

    @Override
    protected T doRead() throws Exception {

        synchronized (lock) {

            if (results == null || current >= pageSize) {

                if (logger.isDebugEnabled()) {
                    logger.debug("Reading page " + getPage());
                }

                doReadPage();
                page++;
                if (current >= pageSize) {
                    current = 0;
                }

            }

            if(results instanceof List) {
                return (T) results;
            } else {
                return null;
            }

        }

    }

    abstract protected void doReadPage();

    @Override
    protected void doOpen() throws Exception {

        Assert.state(!initialized, "Cannot open an already opened ItemReader, call close first");
        initialized = true;

    }

    @Override
    protected void doClose() throws Exception {

        synchronized (lock) {
            initialized = false;
            current = 0;
            page = 0;
            results = null;
        }

    }

    @Override
    protected void jumpToItem(int itemIndex) throws Exception {

        synchronized (lock) {
            page = itemIndex / pageSize;
            current = itemIndex % pageSize;
        }

        doJumpToPage(itemIndex);

        if (logger.isDebugEnabled()) {
            logger.debug("Jumping to page " + getPage() + " and index " + current);
        }

    }

    abstract protected void doJumpToPage(int itemIndex);
}
