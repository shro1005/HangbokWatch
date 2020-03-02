package com.hangbokwatch.backend.job.batch.allplayerrefrsh;

import com.hangbokwatch.backend.domain.hero.*;
import com.hangbokwatch.backend.domain.player.Player;
import com.hangbokwatch.backend.domain.player.PlayerDetail;
import com.hangbokwatch.backend.domain.player.PlayerForRanking;
import com.hangbokwatch.backend.domain.player.Trendline;
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

public class CrawlingDataJpaItemWriter<T> implements ItemWriter<T>, InitializingBean {
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
                if(item instanceof CompetitiveDetailDto) {
                    Player player = ((CompetitiveDetailDto) item).getPlayer();
                    Trendline trendline = ((CompetitiveDetailDto) item).getTrendline();
                    List<PlayerDetail> playerDetailList = ((CompetitiveDetailDto) item).getPlayerDetailList();
                    PlayerForRanking playerForRanking = ((CompetitiveDetailDto) item).getPlayerForRanking();

                    //탱커
                    Dva dva = ((CompetitiveDetailDto) item).getDva();
                    Orisa orisa = ((CompetitiveDetailDto) item).getOrisa();
                    Reinhardt reinhardt = ((CompetitiveDetailDto) item).getReinhardt();
                    Zarya zarya = ((CompetitiveDetailDto) item).getZarya();
                    RoadHog roadHog = ((CompetitiveDetailDto) item).getRoadHog();
                    Winston winston = ((CompetitiveDetailDto) item).getWinston();
                    Sigma sigma = ((CompetitiveDetailDto) item).getSigma();
                    WreckingBall wreckingBall = ((CompetitiveDetailDto) item).getWreckingBall();

                    //힐러
                    Ana ana = ((CompetitiveDetailDto) item).getAna();
                    Baptiste baptiste = ((CompetitiveDetailDto) item).getBaptiste();
                    Brigitte brigitte = ((CompetitiveDetailDto) item).getBrigitte();
                    Lucio lucio = ((CompetitiveDetailDto) item).getLucio();
                    Mercy mercy = ((CompetitiveDetailDto) item).getMercy();
                    Moira moira = ((CompetitiveDetailDto) item).getMoira();
                    Zenyatta zenyatta = ((CompetitiveDetailDto) item).getZenyatta();

                    //딜러
                    Junkrat junkrat = ((CompetitiveDetailDto) item).getJunkrat();
                    Genji genji = ((CompetitiveDetailDto) item).getGenji();
                    Doomfist doomfist = ((CompetitiveDetailDto) item).getDoomfist();
                    Reaper reaper = ((CompetitiveDetailDto) item).getReaper();
                    Mccree mccree = ((CompetitiveDetailDto) item).getMccree();
                    Mei mei = ((CompetitiveDetailDto) item).getMei();
                    Bastion bastion = ((CompetitiveDetailDto) item).getBastion();
                    Soldier76 soldier76 = ((CompetitiveDetailDto) item).getSoldier76();
                    Sombra sombra = ((CompetitiveDetailDto) item).getSombra();
                    Symmetra symmetra = ((CompetitiveDetailDto) item).getSymmetra();
                    Widowmaker widowmaker = ((CompetitiveDetailDto) item).getWidowmaker();
                    Ashe ashe = ((CompetitiveDetailDto) item).getAshe();
                    Torbjorn torbjorn = ((CompetitiveDetailDto) item).getTorbjorn();
                    Tracer tracer = ((CompetitiveDetailDto) item).getTracer();
                    Pharah pharah = ((CompetitiveDetailDto) item).getPharah();
                    Hanzo hanzo = ((CompetitiveDetailDto) item).getHanzo();


                    entityManager.merge(player);
                    mergeCount++;

                    if (trendline != null) {
                        entityManager.merge(trendline);
                        mergeCount++;
                    }
                    if (playerDetailList != null && playerDetailList.size() > 0) {
                        for (PlayerDetail playerDetail : playerDetailList) {
                            entityManager.merge(playerDetail);
                            mergeCount ++;
                        }
                    }
                    if(playerForRanking != null) {
                        entityManager.merge(playerForRanking);
                        mergeCount++;
                    }

                    if (dva != null) {
                        entityManager.merge(dva);
                        mergeCount++;
                    }
                    if (orisa != null) {
                        entityManager.merge(orisa);
                        mergeCount++;
                    }
                    if (reinhardt != null) {
                        entityManager.merge(reinhardt);
                        mergeCount++;
                    }
                    if (zarya != null) {
                        entityManager.merge(zarya);
                        mergeCount++;
                    }
                    if (roadHog != null) {
                        entityManager.merge(roadHog);
                        mergeCount++;
                    }
                    if (winston != null) {
                        entityManager.merge(winston);
                        mergeCount++;
                    }
                    if (sigma != null) {
                        entityManager.merge(sigma);
                        mergeCount++;
                    }
                    if (wreckingBall != null) {
                        entityManager.merge(wreckingBall);
                        mergeCount++;
                    }
                    if (ana != null) {
                        entityManager.merge(ana);
                        mergeCount++;
                    }
                    if (baptiste != null) {
                        entityManager.merge(baptiste);
                        mergeCount++;
                    }
                    if (brigitte != null) {
                        entityManager.merge(brigitte);
                        mergeCount++;
                    }
                    if (lucio != null) {
                        entityManager.merge(lucio);
                        mergeCount++;
                    }
                    if (mercy != null) {
                        entityManager.merge(mercy);
                        mergeCount++;
                    }
                    if (moira != null) {
                        entityManager.merge(moira);
                        mergeCount++;
                    }
                    if (zenyatta != null) {
                        entityManager.merge(zenyatta);
                        mergeCount++;
                    }
                    if (junkrat != null) {
                        entityManager.merge(junkrat);
                        mergeCount++;
                    }
                    if (genji != null) {
                        entityManager.merge(genji);
                        mergeCount++;
                    }
                    if (doomfist != null) {
                        entityManager.merge(doomfist);
                        mergeCount++;
                    }
                    if (reaper != null) {
                        entityManager.merge(reaper);
                        mergeCount++;
                    }
                    if (mccree != null) {
                        entityManager.merge(mccree);
                        mergeCount++;
                    }
                    if (mei != null) {
                        entityManager.merge(mei);
                        mergeCount++;
                    }
                    if (bastion != null) {
                        entityManager.merge(bastion);
                        mergeCount++;
                    }
                    if (soldier76 != null) {
                        entityManager.merge(soldier76);
                        mergeCount++;
                    }
                    if (sombra != null) {
                        entityManager.merge(sombra);
                        mergeCount++;
                    }
                    if (symmetra != null) {
                        entityManager.merge(symmetra);
                        mergeCount++;
                    }
                    if (widowmaker != null) {
                        entityManager.merge(widowmaker);
                        mergeCount++;
                    }
                    if (torbjorn != null) {
                        entityManager.merge(torbjorn);
                        mergeCount++;
                    }
                    if (tracer != null) {
                        entityManager.merge(tracer);
                        mergeCount++;
                    }
                    if (ashe != null) {
                        entityManager.merge(ashe);
                        mergeCount++;
                    }
                    if (pharah != null) {
                        entityManager.merge(pharah);
                        mergeCount++;
                    }
                    if (hanzo != null) {
                        entityManager.merge(hanzo);
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
