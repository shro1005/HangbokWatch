package com.hangbokwatch.backend.job.batch.rankingupdate;

import com.hangbokwatch.backend.domain.player.Player;
import com.hangbokwatch.backend.domain.player.PlayerForRanking;
import com.hangbokwatch.backend.dto.CompetitiveDetailDto;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

public class RankingBaseUpdateBatchWriter<T> implements ItemWriter<T>, InitializingBean {

    protected static final Log logger = LogFactory.getLog(JpaItemWriter.class);

    private EntityManagerFactory entityManagerFactory;

    /**
     * Set the EntityManager to be used internally.
     *
     * @param entityManagerFactory the entityManagerFactory to set
     */
    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(entityManagerFactory, "An EntityManagerFactory is required");
    }

    /**
     * Merge all provided items that aren't already in the persistence context
     * and then flush the entity manager.
     *
     * @see org.springframework.batch.item.ItemWriter#write(java.util.List)
     */
    @Override
    public void write(List<? extends T> items) {
        EntityManager entityManager = EntityManagerFactoryUtils.getTransactionalEntityManager(entityManagerFactory);
        if (entityManager == null) {
            throw new DataAccessResourceFailureException("Unable to obtain a transactional EntityManager");
        }
        doWrite(entityManager, items);
        entityManager.flush();
    }

    /**
     * Do perform the actual write operation. This can be overridden in a
     * subclass if necessary.
     *
     * @param entityManager the EntityManager to use for the operation
     * @param items the list of items to use for the write
     */
    protected void doWrite(EntityManager entityManager, List<? extends T> items) {

        if (logger.isDebugEnabled()) {
            logger.debug("Writing to JPA with " + items.size() + " items.");
        }

        if (!items.isEmpty()) {
            long mergeCount = 0;
            for (T item : items) {
                if (item instanceof CompetitiveDetailDto) {
                    Player player = ((CompetitiveDetailDto) item).getPlayer();
                    PlayerForRanking playerForRanking = ((CompetitiveDetailDto) item).getPlayerForRanking();

                    if(player != null) {
                        entityManager.merge(player);
                        mergeCount++;
                    }

                    if(playerForRanking != null) {
                        entityManager.merge(playerForRanking);
                        mergeCount++;
                    }
                }
            }
            if (logger.isDebugEnabled()) {
                logger.debug(mergeCount + " entities merged.");
                logger.debug((items.size() - mergeCount) + " entities found in persistence context.");
            }
        }
    }
}
