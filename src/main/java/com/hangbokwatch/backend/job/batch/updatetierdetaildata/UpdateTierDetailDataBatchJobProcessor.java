package com.hangbokwatch.backend.job.batch.updatetierdetaildata;

import com.hangbokwatch.backend.dao.player.PlayerDetailRepository;
import com.hangbokwatch.backend.domain.player.Player;
import com.hangbokwatch.backend.domain.player.PlayerDetail;
import com.hangbokwatch.backend.dto.CompetitiveDetailDto;
import com.hangbokwatch.backend.dto.PlayerDetailDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
//@StepScope
public class UpdateTierDetailDataBatchJobProcessor implements ItemProcessor<List<Player>, CompetitiveDetailDto> {
    public static final String JOB_NAME = "updateTierDetailDataBatch";
    @Value("${spring.HWresource.HWimages}")
    private String portraitPath;

//    @Value("#{jobParameters[season]}") Double season;
    @Autowired
    PlayerDetailRepository playerDetailRepository;

    private long season = 0;
    private int min = 0;
    private int max = 0;

    @Override
    public CompetitiveDetailDto process(List<Player> items) throws Exception {
        CompetitiveDetailDto cdDto = new CompetitiveDetailDto();
        List<PlayerDetail> playerDetailList = new ArrayList<>();
        log.debug("{} ============================================= Processor start ======================================", JOB_NAME);
        double[][] datas = new double[40][13];

        int anaCnt = 0;
        int asheCnt = 0;
        int baptisteCnt = 0;
        int bastionCnt = 0;
        int brigitteCnt = 0;
        int doomfistCnt = 0;
        int dvaCnt = 0;
        int genjiCnt = 0;
        int hanzoCnt = 0;
        int junkratCnt = 0;
        int lucioCnt = 0;
        int mccreeCnt = 0;
        int meiCnt = 0;
        int mercyCnt = 0;
        int moiraCnt = 0;
        int orisaCnt = 0;
        int pharahCnt = 0;
        int reaperCnt = 0;
        int reinhardtCnt = 0;
        int roadhogCnt = 0;
        int sigmaCnt = 0;
        int soldier76Cnt = 0;
        int sombraCnt = 0;
        int symmetraCnt = 0;
        int torbjornCnt = 0;
        int tracerCnt = 0;
        int widowmakerCnt = 0;
        int winstonCnt = 0;
        int wreckingballCnt = 0;
        int zaryaCnt = 0;
        int zenyattaCnt = 0;

        for (Player player : items) {
            log.debug("{} >>>>>>> process 진행중 | {} 의 데이터 추출 시작", JOB_NAME, player.getBattleTag());
            List<PlayerDetail> playerDetails = playerDetailRepository.findByIdAndSeasonOrderByHeroOrderAsc(player.getId(), season);
            log.debug("{} >>>>>>> process 진행중 | {} 의 디테일 데이터 추출 {}건 ", JOB_NAME, player.getBattleTag(), playerDetails.size());
            if(player.getDealRatingPoint() >= min && player.getDealRatingPoint() <= max) {
                for (PlayerDetail playerDetail : playerDetails) {
                    switch (playerDetail.getHeroNameKR()) {
                        case "솔저: 76":
                            datas[0][0] += Double.parseDouble(playerDetail.getKillPerDeath());
                            datas[0][1] += Double.parseDouble(playerDetail.getDeathAvg());
                            if (!"".equals(playerDetail.getHealPerLife()) && playerDetail.getHealPerLife() != null) {
                                datas[0][2] += Double.parseDouble(playerDetail.getHealPerLife());
                            }
                            if (!"".equals(playerDetail.getBlockDamagePerLife()) && playerDetail.getBlockDamagePerLife() != null) {
                                datas[0][3] += Double.parseDouble(playerDetail.getBlockDamagePerLife());
                            }
                            if (!"".equals(playerDetail.getLastHitPerLife()) && playerDetail.getLastHitPerLife() != null) {
                                datas[0][4] += Double.parseDouble(playerDetail.getLastHitPerLife());
                            }
                            if (!"".equals(playerDetail.getDamageToHeroPerLife()) && playerDetail.getDamageToHeroPerLife() != null) {
                                datas[0][5] += Double.parseDouble(playerDetail.getDamageToHeroPerLife());
                            }
                            if (!"".equals(playerDetail.getDamageToShieldPerLife()) && playerDetail.getDamageToShieldPerLife() != null) {
                                datas[0][6] += Double.parseDouble(playerDetail.getDamageToShieldPerLife());
                            }
                            if (playerDetail.getIndex1().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex1()) && playerDetail.getIndex1() != null) {
                                    datas[0][7] += Double.parseDouble(playerDetail.getIndex1());
                                }
                            } else {
                                datas[0][7] += Double.parseDouble(playerDetail.getIndex1().replace("%", ""));
                            }
                            if (playerDetail.getIndex2().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex2()) && playerDetail.getIndex2() != null) {
                                    datas[0][8] += Double.parseDouble(playerDetail.getIndex2());
                                }
                            } else {
                                datas[0][8] += Double.parseDouble(playerDetail.getIndex2().replace("%", ""));
                            }
                            if (playerDetail.getIndex3().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex3()) && playerDetail.getIndex3() != null) {
                                    datas[0][9] += Double.parseDouble(playerDetail.getIndex3());
                                }
                            } else {
                                datas[0][9] += Double.parseDouble(playerDetail.getIndex3().replace("%", ""));
                            }
                            if (playerDetail.getIndex4().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex4()) && playerDetail.getIndex4() != null) {
                                    datas[0][10] += Double.parseDouble(playerDetail.getIndex4());
                                }
                            } else {
                                datas[0][10] += Double.parseDouble(playerDetail.getIndex4().replace("%", ""));
                            }
                            if (playerDetail.getIndex5().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex5()) && playerDetail.getIndex5() != null) {
                                    datas[0][11] += Double.parseDouble(playerDetail.getIndex5());
                                }
                            } else {
                                datas[0][11] += Double.parseDouble(playerDetail.getIndex5().replace("%", ""));
                            }

                            soldier76Cnt++;
                            break;
                        case "리퍼":
                            datas[1][0] += Double.parseDouble(playerDetail.getKillPerDeath());
                            datas[1][1] += Double.parseDouble(playerDetail.getDeathAvg());
                            if (!"".equals(playerDetail.getHealPerLife()) && playerDetail.getHealPerLife() != null) {
                                datas[1][2] += Double.parseDouble(playerDetail.getHealPerLife());
                            }
                            if (!"".equals(playerDetail.getBlockDamagePerLife()) && playerDetail.getBlockDamagePerLife() != null) {
                                datas[1][3] += Double.parseDouble(playerDetail.getBlockDamagePerLife());
                            }
                            if (!"".equals(playerDetail.getLastHitPerLife()) && playerDetail.getLastHitPerLife() != null) {
                                datas[1][4] += Double.parseDouble(playerDetail.getLastHitPerLife());
                            }
                            if (!"".equals(playerDetail.getDamageToHeroPerLife()) && playerDetail.getDamageToHeroPerLife() != null) {
                                datas[1][5] += Double.parseDouble(playerDetail.getDamageToHeroPerLife());
                            }
                            if (!"".equals(playerDetail.getDamageToShieldPerLife()) && playerDetail.getDamageToShieldPerLife() != null) {
                                datas[1][6] += Double.parseDouble(playerDetail.getDamageToShieldPerLife());
                            }
                            if (playerDetail.getIndex1().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex1()) && playerDetail.getIndex1() != null) {
                                    datas[1][7] += Double.parseDouble(playerDetail.getIndex1());
                                }
                            } else {
                                datas[1][7] += Double.parseDouble(playerDetail.getIndex1().replace("%", ""));
                            }
                            if (playerDetail.getIndex2().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex2()) && playerDetail.getIndex2() != null) {
                                    datas[1][8] += Double.parseDouble(playerDetail.getIndex2());
                                }
                            } else {
                                datas[1][8] += Double.parseDouble(playerDetail.getIndex2().replace("%", ""));
                            }
                            if (playerDetail.getIndex3().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex3()) && playerDetail.getIndex3() != null) {
                                    datas[1][9] += Double.parseDouble(playerDetail.getIndex3());
                                }
                            } else {
                                datas[1][9] += Double.parseDouble(playerDetail.getIndex3().replace("%", ""));
                            }
                            if (playerDetail.getIndex4().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex4()) && playerDetail.getIndex4() != null) {
                                    datas[1][10] += Double.parseDouble(playerDetail.getIndex4());
                                }
                            } else {
                                datas[1][10] += Double.parseDouble(playerDetail.getIndex4().replace("%", ""));
                            }
                            if (playerDetail.getIndex5().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex5()) && playerDetail.getIndex5() != null) {
                                    datas[1][11] += Double.parseDouble(playerDetail.getIndex5());
                                }
                            } else {
                                datas[1][11] += Double.parseDouble(playerDetail.getIndex5().replace("%", ""));
                            }

                            reaperCnt++;
                            break;
                        case "정크랫":
                            datas[2][0] += Double.parseDouble(playerDetail.getKillPerDeath());
                            datas[2][1] += Double.parseDouble(playerDetail.getDeathAvg());
                            if (!"".equals(playerDetail.getHealPerLife()) && playerDetail.getHealPerLife() != null) {
                                datas[2][2] += Double.parseDouble(playerDetail.getHealPerLife());
                            }
                            if (!"".equals(playerDetail.getBlockDamagePerLife()) && playerDetail.getBlockDamagePerLife() != null) {
                                datas[2][3] += Double.parseDouble(playerDetail.getBlockDamagePerLife());
                            }
                            if (!"".equals(playerDetail.getLastHitPerLife()) && playerDetail.getLastHitPerLife() != null) {
                                datas[2][4] += Double.parseDouble(playerDetail.getLastHitPerLife());
                            }
                            if (!"".equals(playerDetail.getDamageToHeroPerLife()) && playerDetail.getDamageToHeroPerLife() != null) {
                                datas[2][5] += Double.parseDouble(playerDetail.getDamageToHeroPerLife());
                            }
                            if (!"".equals(playerDetail.getDamageToShieldPerLife()) && playerDetail.getDamageToShieldPerLife() != null) {
                                datas[2][6] += Double.parseDouble(playerDetail.getDamageToShieldPerLife());
                            }
                            if (playerDetail.getIndex1().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex1()) && playerDetail.getIndex1() != null) {
                                    datas[2][7] += Double.parseDouble(playerDetail.getIndex1());
                                }
                            } else {
                                datas[2][7] += Double.parseDouble(playerDetail.getIndex1().replace("%", ""));
                            }
                            if (playerDetail.getIndex2().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex2()) && playerDetail.getIndex2() != null) {
                                    datas[2][8] += Double.parseDouble(playerDetail.getIndex2());
                                }
                            } else {
                                datas[2][8] += Double.parseDouble(playerDetail.getIndex2().replace("%", ""));
                            }
                            if (playerDetail.getIndex3().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex3()) && playerDetail.getIndex3() != null) {
                                    datas[2][9] += Double.parseDouble(playerDetail.getIndex3());
                                }
                            } else {
                                datas[2][9] += Double.parseDouble(playerDetail.getIndex3().replace("%", ""));
                            }
                            if (playerDetail.getIndex4().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex4()) && playerDetail.getIndex4() != null) {
                                    datas[2][10] += Double.parseDouble(playerDetail.getIndex4());
                                }
                            } else {
                                datas[2][10] += Double.parseDouble(playerDetail.getIndex4().replace("%", ""));
                            }
                            if (playerDetail.getIndex5().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex5()) && playerDetail.getIndex5() != null) {
                                    datas[2][11] += Double.parseDouble(playerDetail.getIndex5());
                                }
                            } else {
                                datas[2][11] += Double.parseDouble(playerDetail.getIndex5().replace("%", ""));
                            }

                            junkratCnt++;
                            break;
                        case "위도우메이커":
                            datas[3][0] += Double.parseDouble(playerDetail.getKillPerDeath());
                            datas[3][1] += Double.parseDouble(playerDetail.getDeathAvg());
                            if (!"".equals(playerDetail.getHealPerLife()) && playerDetail.getHealPerLife() != null) {
                                datas[3][2] += Double.parseDouble(playerDetail.getHealPerLife());
                            }
                            if (!"".equals(playerDetail.getBlockDamagePerLife()) && playerDetail.getBlockDamagePerLife() != null) {
                                datas[3][3] += Double.parseDouble(playerDetail.getBlockDamagePerLife());
                            }
                            if (!"".equals(playerDetail.getLastHitPerLife()) && playerDetail.getLastHitPerLife() != null) {
                                datas[3][4] += Double.parseDouble(playerDetail.getLastHitPerLife());
                            }
                            if (!"".equals(playerDetail.getDamageToHeroPerLife()) && playerDetail.getDamageToHeroPerLife() != null) {
                                datas[3][5] += Double.parseDouble(playerDetail.getDamageToHeroPerLife());
                            }
                            if (!"".equals(playerDetail.getDamageToShieldPerLife()) && playerDetail.getDamageToShieldPerLife() != null) {
                                datas[3][6] += Double.parseDouble(playerDetail.getDamageToShieldPerLife());
                            }
                            if (playerDetail.getIndex1().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex1()) && playerDetail.getIndex1() != null) {
                                    datas[3][7] += Double.parseDouble(playerDetail.getIndex1());
                                }
                            } else {
                                datas[3][7] += Double.parseDouble(playerDetail.getIndex1().replace("%", ""));
                            }
                            if (playerDetail.getIndex2().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex2()) && playerDetail.getIndex2() != null) {
                                    datas[3][8] += Double.parseDouble(playerDetail.getIndex2());
                                }
                            } else {
                                datas[3][8] += Double.parseDouble(playerDetail.getIndex2().replace("%", ""));
                            }
                            if (playerDetail.getIndex3().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex3()) && playerDetail.getIndex3() != null) {
                                    datas[3][9] += Double.parseDouble(playerDetail.getIndex3());
                                }
                            } else {
                                datas[3][9] += Double.parseDouble(playerDetail.getIndex3().replace("%", ""));
                            }
                            if (playerDetail.getIndex4().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex4()) && playerDetail.getIndex4() != null) {
                                    datas[3][10] += Double.parseDouble(playerDetail.getIndex4());
                                }
                            } else {
                                datas[3][10] += Double.parseDouble(playerDetail.getIndex4().replace("%", ""));
                            }
                            if (playerDetail.getIndex5().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex5()) && playerDetail.getIndex5() != null) {
                                    datas[3][11] += Double.parseDouble(playerDetail.getIndex5());
                                }
                            } else {
                                datas[3][11] += Double.parseDouble(playerDetail.getIndex5().replace("%", ""));
                            }

                            widowmakerCnt++;
                            break;
                        case "토르비욘":
                            datas[4][0] += Double.parseDouble(playerDetail.getKillPerDeath());
                            datas[4][1] += Double.parseDouble(playerDetail.getDeathAvg());
                            if (!"".equals(playerDetail.getHealPerLife()) && playerDetail.getHealPerLife() != null) {
                                datas[4][2] += Double.parseDouble(playerDetail.getHealPerLife());
                            }
                            if (!"".equals(playerDetail.getBlockDamagePerLife()) && playerDetail.getBlockDamagePerLife() != null) {
                                datas[4][3] += Double.parseDouble(playerDetail.getBlockDamagePerLife());
                            }
                            if (!"".equals(playerDetail.getLastHitPerLife()) && playerDetail.getLastHitPerLife() != null) {
                                datas[4][4] += Double.parseDouble(playerDetail.getLastHitPerLife());
                            }
                            if (!"".equals(playerDetail.getDamageToHeroPerLife()) && playerDetail.getDamageToHeroPerLife() != null) {
                                datas[4][5] += Double.parseDouble(playerDetail.getDamageToHeroPerLife());
                            }
                            if (!"".equals(playerDetail.getDamageToShieldPerLife()) && playerDetail.getDamageToShieldPerLife() != null) {
                                datas[4][6] += Double.parseDouble(playerDetail.getDamageToShieldPerLife());
                            }
                            if (playerDetail.getIndex1().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex1()) && playerDetail.getIndex1() != null) {
                                    datas[4][7] += Double.parseDouble(playerDetail.getIndex1());
                                }
                            } else {
                                datas[4][7] += Double.parseDouble(playerDetail.getIndex1().replace("%", ""));
                            }
                            if (playerDetail.getIndex2().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex2()) && playerDetail.getIndex2() != null) {
                                    datas[4][8] += Double.parseDouble(playerDetail.getIndex2());
                                }
                            } else {
                                datas[4][8] += Double.parseDouble(playerDetail.getIndex2().replace("%", ""));
                            }
                            if (playerDetail.getIndex3().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex3()) && playerDetail.getIndex3() != null) {
                                    datas[4][9] += Double.parseDouble(playerDetail.getIndex3());
                                }
                            } else {
                                datas[4][9] += Double.parseDouble(playerDetail.getIndex3().replace("%", ""));
                            }
                            if (playerDetail.getIndex4().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex4()) && playerDetail.getIndex4() != null) {
                                    datas[4][10] += Double.parseDouble(playerDetail.getIndex4());
                                }
                            } else {
                                datas[4][10] += Double.parseDouble(playerDetail.getIndex4().replace("%", ""));
                            }
                            if (playerDetail.getIndex5().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex5()) && playerDetail.getIndex5() != null) {
                                    datas[4][11] += Double.parseDouble(playerDetail.getIndex5());
                                }
                            } else {
                                datas[4][11] += Double.parseDouble(playerDetail.getIndex5().replace("%", ""));
                            }

                            torbjornCnt ++ ;
                            break;
                        case "트레이서":
                            datas[5][0] += Double.parseDouble(playerDetail.getKillPerDeath());
                            datas[5][1] += Double.parseDouble(playerDetail.getDeathAvg());
                            if (!"".equals(playerDetail.getHealPerLife()) && playerDetail.getHealPerLife() != null) {
                                datas[5][2] += Double.parseDouble(playerDetail.getHealPerLife());
                            }
                            if (!"".equals(playerDetail.getBlockDamagePerLife()) && playerDetail.getBlockDamagePerLife() != null) {
                                datas[5][3] += Double.parseDouble(playerDetail.getBlockDamagePerLife());
                            }
                            if (!"".equals(playerDetail.getLastHitPerLife()) && playerDetail.getLastHitPerLife() != null) {
                                datas[5][4] += Double.parseDouble(playerDetail.getLastHitPerLife());
                            }
                            if (!"".equals(playerDetail.getDamageToHeroPerLife()) && playerDetail.getDamageToHeroPerLife() != null) {
                                datas[5][5] += Double.parseDouble(playerDetail.getDamageToHeroPerLife());
                            }
                            if (!"".equals(playerDetail.getDamageToShieldPerLife()) && playerDetail.getDamageToShieldPerLife() != null) {
                                datas[5][6] += Double.parseDouble(playerDetail.getDamageToShieldPerLife());
                            }
                            if (playerDetail.getIndex1().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex1()) && playerDetail.getIndex1() != null) {
                                    datas[5][7] += Double.parseDouble(playerDetail.getIndex1());
                                }
                            } else {
                                datas[5][7] += Double.parseDouble(playerDetail.getIndex1().replace("%", ""));
                            }
                            if (playerDetail.getIndex2().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex2()) && playerDetail.getIndex2() != null) {
                                    datas[5][8] += Double.parseDouble(playerDetail.getIndex2());
                                }
                            } else {
                                datas[5][8] += Double.parseDouble(playerDetail.getIndex2().replace("%", ""));
                            }
                            if (playerDetail.getIndex3().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex3()) && playerDetail.getIndex3() != null) {
                                    datas[5][9] += Double.parseDouble(playerDetail.getIndex3());
                                }
                            } else {
                                datas[5][9] += Double.parseDouble(playerDetail.getIndex3().replace("%", ""));
                            }
                            if (playerDetail.getIndex4().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex4()) && playerDetail.getIndex4() != null) {
                                    datas[5][10] += Double.parseDouble(playerDetail.getIndex4());
                                }
                            } else {
                                datas[5][10] += Double.parseDouble(playerDetail.getIndex4().replace("%", ""));
                            }
                            if (playerDetail.getIndex5().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex5()) && playerDetail.getIndex5() != null) {
                                    datas[5][11] += Double.parseDouble(playerDetail.getIndex5());
                                }
                            } else {
                                datas[5][11] += Double.parseDouble(playerDetail.getIndex5().replace("%", ""));
                            }
                            tracerCnt ++;
                            break;
                        case "파라":
                            datas[6][0] += Double.parseDouble(playerDetail.getKillPerDeath());
                            datas[6][1] += Double.parseDouble(playerDetail.getDeathAvg());
                            if (!"".equals(playerDetail.getHealPerLife()) && playerDetail.getHealPerLife() != null) {
                                datas[6][2] += Double.parseDouble(playerDetail.getHealPerLife());
                            }
                            if (!"".equals(playerDetail.getBlockDamagePerLife()) && playerDetail.getBlockDamagePerLife() != null) {
                                datas[6][3] += Double.parseDouble(playerDetail.getBlockDamagePerLife());
                            }
                            if (!"".equals(playerDetail.getLastHitPerLife()) && playerDetail.getLastHitPerLife() != null) {
                                datas[6][4] += Double.parseDouble(playerDetail.getLastHitPerLife());
                            }
                            if (!"".equals(playerDetail.getDamageToHeroPerLife()) && playerDetail.getDamageToHeroPerLife() != null) {
                                datas[6][5] += Double.parseDouble(playerDetail.getDamageToHeroPerLife());
                            }
                            if (!"".equals(playerDetail.getDamageToShieldPerLife()) && playerDetail.getDamageToShieldPerLife() != null) {
                                datas[6][6] += Double.parseDouble(playerDetail.getDamageToShieldPerLife());
                            }
                            if (playerDetail.getIndex1().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex1()) && playerDetail.getIndex1() != null) {
                                    datas[6][7] += Double.parseDouble(playerDetail.getIndex1());
                                }
                            } else {
                                datas[6][7] += Double.parseDouble(playerDetail.getIndex1().replace("%", ""));
                            }
                            if (playerDetail.getIndex2().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex2()) && playerDetail.getIndex2() != null) {
                                    datas[6][8] += Double.parseDouble(playerDetail.getIndex2());
                                }
                            } else {
                                datas[6][8] += Double.parseDouble(playerDetail.getIndex2().replace("%", ""));
                            }
                            if (playerDetail.getIndex3().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex3()) && playerDetail.getIndex3() != null) {
                                    datas[6][9] += Double.parseDouble(playerDetail.getIndex3());
                                }
                            } else {
                                datas[6][9] += Double.parseDouble(playerDetail.getIndex3().replace("%", ""));
                            }
                            if (playerDetail.getIndex4().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex4()) && playerDetail.getIndex4() != null) {
                                    datas[6][10] += Double.parseDouble(playerDetail.getIndex4());
                                }
                            } else {
                                datas[6][10] += Double.parseDouble(playerDetail.getIndex4().replace("%", ""));
                            }
                            if (playerDetail.getIndex5().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex5()) && playerDetail.getIndex5() != null) {
                                    datas[6][11] += Double.parseDouble(playerDetail.getIndex5());
                                }
                            } else {
                                datas[6][11] += Double.parseDouble(playerDetail.getIndex5().replace("%", ""));
                            }
                            pharahCnt ++ ;
                            break;
                        case "한조":
                            datas[7][0] += Double.parseDouble(playerDetail.getKillPerDeath());
                            datas[7][1] += Double.parseDouble(playerDetail.getDeathAvg());
                            if (!"".equals(playerDetail.getHealPerLife()) && playerDetail.getHealPerLife() != null) {
                                datas[7][2] += Double.parseDouble(playerDetail.getHealPerLife());
                            }
                            if (!"".equals(playerDetail.getBlockDamagePerLife()) && playerDetail.getBlockDamagePerLife() != null) {
                                datas[7][3] += Double.parseDouble(playerDetail.getBlockDamagePerLife());
                            }
                            if (!"".equals(playerDetail.getLastHitPerLife()) && playerDetail.getLastHitPerLife() != null) {
                                datas[7][4] += Double.parseDouble(playerDetail.getLastHitPerLife());
                            }
                            if (!"".equals(playerDetail.getDamageToHeroPerLife()) && playerDetail.getDamageToHeroPerLife() != null) {
                                datas[7][5] += Double.parseDouble(playerDetail.getDamageToHeroPerLife());
                            }
                            if (!"".equals(playerDetail.getDamageToShieldPerLife()) && playerDetail.getDamageToShieldPerLife() != null) {
                                datas[7][6] += Double.parseDouble(playerDetail.getDamageToShieldPerLife());
                            }
                            if (playerDetail.getIndex1().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex1()) && playerDetail.getIndex1() != null) {
                                    datas[7][7] += Double.parseDouble(playerDetail.getIndex1());
                                }
                            } else {
                                datas[7][7] += Double.parseDouble(playerDetail.getIndex1().replace("%", ""));
                            }
                            if (playerDetail.getIndex2().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex2()) && playerDetail.getIndex2() != null) {
                                    datas[7][8] += Double.parseDouble(playerDetail.getIndex2());
                                }
                            } else {
                                datas[7][8] += Double.parseDouble(playerDetail.getIndex2().replace("%", ""));
                            }
                            if (playerDetail.getIndex3().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex3()) && playerDetail.getIndex3() != null) {
                                    datas[7][9] += Double.parseDouble(playerDetail.getIndex3());
                                }
                            } else {
                                datas[7][9] += Double.parseDouble(playerDetail.getIndex3().replace("%", ""));
                            }
                            if (playerDetail.getIndex4().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex4()) && playerDetail.getIndex4() != null) {
                                    datas[7][10] += Double.parseDouble(playerDetail.getIndex4());
                                }
                            } else {
                                datas[7][10] += Double.parseDouble(playerDetail.getIndex4().replace("%", ""));
                            }
                            if (playerDetail.getIndex5().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex5()) && playerDetail.getIndex5() != null) {
                                    datas[7][11] += Double.parseDouble(playerDetail.getIndex5());
                                }
                            } else {
                                datas[7][11] += Double.parseDouble(playerDetail.getIndex5().replace("%", ""));
                            }
                            hanzoCnt ++;
                            break;
                        case "애쉬":
                            datas[8][0] += Double.parseDouble(playerDetail.getKillPerDeath());
                            datas[8][1] += Double.parseDouble(playerDetail.getDeathAvg());
                            if (!"".equals(playerDetail.getHealPerLife()) && playerDetail.getHealPerLife() != null) {
                                datas[8][2] += Double.parseDouble(playerDetail.getHealPerLife());
                            }
                            if (!"".equals(playerDetail.getBlockDamagePerLife()) && playerDetail.getBlockDamagePerLife() != null) {
                                datas[8][3] += Double.parseDouble(playerDetail.getBlockDamagePerLife());
                            }
                            if (!"".equals(playerDetail.getLastHitPerLife()) && playerDetail.getLastHitPerLife() != null) {
                                datas[8][4] += Double.parseDouble(playerDetail.getLastHitPerLife());
                            }
                            if (!"".equals(playerDetail.getDamageToHeroPerLife()) && playerDetail.getDamageToHeroPerLife() != null) {
                                datas[8][5] += Double.parseDouble(playerDetail.getDamageToHeroPerLife());
                            }
                            if (!"".equals(playerDetail.getDamageToShieldPerLife()) && playerDetail.getDamageToShieldPerLife() != null) {
                                datas[8][6] += Double.parseDouble(playerDetail.getDamageToShieldPerLife());
                            }
                            if (playerDetail.getIndex1().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex1()) && playerDetail.getIndex1() != null) {
                                    datas[8][7] += Double.parseDouble(playerDetail.getIndex1());
                                }
                            } else {
                                datas[8][7] += Double.parseDouble(playerDetail.getIndex1().replace("%", ""));
                            }
                            if (playerDetail.getIndex2().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex2()) && playerDetail.getIndex2() != null) {
                                    datas[8][8] += Double.parseDouble(playerDetail.getIndex2());
                                }
                            } else {
                                datas[8][8] += Double.parseDouble(playerDetail.getIndex2().replace("%", ""));
                            }
                            if (playerDetail.getIndex3().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex3()) && playerDetail.getIndex3() != null) {
                                    datas[8][9] += Double.parseDouble(playerDetail.getIndex3());
                                }
                            } else {
                                datas[8][9] += Double.parseDouble(playerDetail.getIndex3().replace("%", ""));
                            }
                            if (playerDetail.getIndex4().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex4()) && playerDetail.getIndex4() != null) {
                                    datas[8][10] += Double.parseDouble(playerDetail.getIndex4());
                                }
                            } else {
                                datas[8][10] += Double.parseDouble(playerDetail.getIndex4().replace("%", ""));
                            }
                            if (playerDetail.getIndex5().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex5()) && playerDetail.getIndex5() != null) {
                                    datas[8][11] += Double.parseDouble(playerDetail.getIndex5());
                                }
                            } else {
                                datas[8][11] += Double.parseDouble(playerDetail.getIndex5().replace("%", ""));
                            }
                            asheCnt ++ ;
                            break;
                        case "시메트라":
                            datas[9][0] += Double.parseDouble(playerDetail.getKillPerDeath());
                            datas[9][1] += Double.parseDouble(playerDetail.getDeathAvg());
                            if (!"".equals(playerDetail.getHealPerLife()) && playerDetail.getHealPerLife() != null) {
                                datas[9][2] += Double.parseDouble(playerDetail.getHealPerLife());
                            }
                            if (!"".equals(playerDetail.getBlockDamagePerLife()) && playerDetail.getBlockDamagePerLife() != null) {
                                datas[9][3] += Double.parseDouble(playerDetail.getBlockDamagePerLife());
                            }
                            if (!"".equals(playerDetail.getLastHitPerLife()) && playerDetail.getLastHitPerLife() != null) {
                                datas[9][4] += Double.parseDouble(playerDetail.getLastHitPerLife());
                            }
                            if (!"".equals(playerDetail.getDamageToHeroPerLife()) && playerDetail.getDamageToHeroPerLife() != null) {
                                datas[9][5] += Double.parseDouble(playerDetail.getDamageToHeroPerLife());
                            }
                            if (!"".equals(playerDetail.getDamageToShieldPerLife()) && playerDetail.getDamageToShieldPerLife() != null) {
                                datas[9][6] += Double.parseDouble(playerDetail.getDamageToShieldPerLife());
                            }
                            if (playerDetail.getIndex1().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex1()) && playerDetail.getIndex1() != null) {
                                    datas[9][7] += Double.parseDouble(playerDetail.getIndex1());
                                }
                            } else {
                                datas[9][7] += Double.parseDouble(playerDetail.getIndex1().replace("%", ""));
                            }
                            if (playerDetail.getIndex2().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex2()) && playerDetail.getIndex2() != null) {
                                    datas[9][8] += Double.parseDouble(playerDetail.getIndex2());
                                }
                            } else {
                                datas[9][8] += Double.parseDouble(playerDetail.getIndex2().replace("%", ""));
                            }
                            if (playerDetail.getIndex3().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex3()) && playerDetail.getIndex3() != null) {
                                    datas[9][9] += Double.parseDouble(playerDetail.getIndex3());
                                }
                            } else {
                                datas[9][9] += Double.parseDouble(playerDetail.getIndex3().replace("%", ""));
                            }
                            if (playerDetail.getIndex4().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex4()) && playerDetail.getIndex4() != null) {
                                    datas[9][10] += Double.parseDouble(playerDetail.getIndex4());
                                }
                            } else {
                                datas[9][10] += Double.parseDouble(playerDetail.getIndex4().replace("%", ""));
                            }
                            if (playerDetail.getIndex5().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex5()) && playerDetail.getIndex5() != null) {
                                    datas[9][11] += Double.parseDouble(playerDetail.getIndex5());
                                }
                            } else {
                                datas[9][11] += Double.parseDouble(playerDetail.getIndex5().replace("%", ""));
                            }
                            symmetraCnt ++;
                            break;
                        case "솜브라":
                            datas[10][0] += Double.parseDouble(playerDetail.getKillPerDeath());
                            datas[10][1] += Double.parseDouble(playerDetail.getDeathAvg());
                            if (!"".equals(playerDetail.getHealPerLife()) && playerDetail.getHealPerLife() != null) {
                                datas[10][2] += Double.parseDouble(playerDetail.getHealPerLife());
                            }
                            if (!"".equals(playerDetail.getBlockDamagePerLife()) && playerDetail.getBlockDamagePerLife() != null) {
                                datas[10][3] += Double.parseDouble(playerDetail.getBlockDamagePerLife());
                            }
                            if (!"".equals(playerDetail.getLastHitPerLife()) && playerDetail.getLastHitPerLife() != null) {
                                datas[10][4] += Double.parseDouble(playerDetail.getLastHitPerLife());
                            }
                            if (!"".equals(playerDetail.getDamageToHeroPerLife()) && playerDetail.getDamageToHeroPerLife() != null) {
                                datas[10][5] += Double.parseDouble(playerDetail.getDamageToHeroPerLife());
                            }
                            if (!"".equals(playerDetail.getDamageToShieldPerLife()) && playerDetail.getDamageToShieldPerLife() != null) {
                                datas[10][6] += Double.parseDouble(playerDetail.getDamageToShieldPerLife());
                            }
                            if (playerDetail.getIndex1().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex1()) && playerDetail.getIndex1() != null) {
                                    datas[10][7] += Double.parseDouble(playerDetail.getIndex1());
                                }
                            } else {
                                datas[10][7] += Double.parseDouble(playerDetail.getIndex1().replace("%", ""));
                            }
                            if (playerDetail.getIndex2().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex2()) && playerDetail.getIndex2() != null) {
                                    datas[10][8] += Double.parseDouble(playerDetail.getIndex2());
                                }
                            } else {
                                datas[10][8] += Double.parseDouble(playerDetail.getIndex2().replace("%", ""));
                            }
                            if (playerDetail.getIndex3().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex3()) && playerDetail.getIndex3() != null) {
                                    datas[10][9] += Double.parseDouble(playerDetail.getIndex3());
                                }
                            } else {
                                datas[10][9] += Double.parseDouble(playerDetail.getIndex3().replace("%", ""));
                            }
                            if (playerDetail.getIndex4().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex4()) && playerDetail.getIndex4() != null) {
                                    datas[10][10] += Double.parseDouble(playerDetail.getIndex4());
                                }
                            } else {
                                datas[10][10] += Double.parseDouble(playerDetail.getIndex4().replace("%", ""));
                            }
                            if (playerDetail.getIndex5().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex5()) && playerDetail.getIndex5() != null) {
                                    datas[10][11] += Double.parseDouble(playerDetail.getIndex5());
                                }
                            } else {
                                datas[10][11] += Double.parseDouble(playerDetail.getIndex5().replace("%", ""));
                            }
                            sombraCnt ++ ;
                            break;
                        case "바스티온":
                            datas[11][0] += Double.parseDouble(playerDetail.getKillPerDeath());
                            datas[11][1] += Double.parseDouble(playerDetail.getDeathAvg());
                            if (!"".equals(playerDetail.getHealPerLife()) && playerDetail.getHealPerLife() != null) {
                                datas[11][2] += Double.parseDouble(playerDetail.getHealPerLife());
                            }
                            if (!"".equals(playerDetail.getBlockDamagePerLife()) && playerDetail.getBlockDamagePerLife() != null) {
                                datas[11][3] += Double.parseDouble(playerDetail.getBlockDamagePerLife());
                            }
                            if (!"".equals(playerDetail.getLastHitPerLife()) && playerDetail.getLastHitPerLife() != null) {
                                datas[11][4] += Double.parseDouble(playerDetail.getLastHitPerLife());
                            }
                            if (!"".equals(playerDetail.getDamageToHeroPerLife()) && playerDetail.getDamageToHeroPerLife() != null) {
                                datas[11][5] += Double.parseDouble(playerDetail.getDamageToHeroPerLife());
                            }
                            if (!"".equals(playerDetail.getDamageToShieldPerLife()) && playerDetail.getDamageToShieldPerLife() != null) {
                                datas[11][6] += Double.parseDouble(playerDetail.getDamageToShieldPerLife());
                            }
                            if (playerDetail.getIndex1().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex1()) && playerDetail.getIndex1() != null) {
                                    datas[11][7] += Double.parseDouble(playerDetail.getIndex1());
                                }
                            } else {
                                datas[11][7] += Double.parseDouble(playerDetail.getIndex1().replace("%", ""));
                            }
                            if (playerDetail.getIndex2().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex2()) && playerDetail.getIndex2() != null) {
                                    datas[11][8] += Double.parseDouble(playerDetail.getIndex2());
                                }
                            } else {
                                datas[11][8] += Double.parseDouble(playerDetail.getIndex2().replace("%", ""));
                            }
                            if (playerDetail.getIndex3().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex3()) && playerDetail.getIndex3() != null) {
                                    datas[11][9] += Double.parseDouble(playerDetail.getIndex3());
                                }
                            } else {
                                datas[11][9] += Double.parseDouble(playerDetail.getIndex3().replace("%", ""));
                            }
                            if (playerDetail.getIndex4().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex4()) && playerDetail.getIndex4() != null) {
                                    datas[11][10] += Double.parseDouble(playerDetail.getIndex4());
                                }
                            } else {
                                datas[11][10] += Double.parseDouble(playerDetail.getIndex4().replace("%", ""));
                            }
                            if (playerDetail.getIndex5().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex5()) && playerDetail.getIndex5() != null) {
                                    datas[11][11] += Double.parseDouble(playerDetail.getIndex5());
                                }
                            } else {
                                datas[11][11] += Double.parseDouble(playerDetail.getIndex5().replace("%", ""));
                            }
                            bastionCnt ++ ;
                            break;
                        case "메이":
                            datas[12][0] += Double.parseDouble(playerDetail.getKillPerDeath());
                            datas[12][1] += Double.parseDouble(playerDetail.getDeathAvg());
                            if (!"".equals(playerDetail.getHealPerLife()) && playerDetail.getHealPerLife() != null) {
                                datas[12][2] += Double.parseDouble(playerDetail.getHealPerLife());
                            }
                            if (!"".equals(playerDetail.getBlockDamagePerLife()) && playerDetail.getBlockDamagePerLife() != null) {
                                datas[12][3] += Double.parseDouble(playerDetail.getBlockDamagePerLife());
                            }
                            if (!"".equals(playerDetail.getLastHitPerLife()) && playerDetail.getLastHitPerLife() != null) {
                                datas[12][4] += Double.parseDouble(playerDetail.getLastHitPerLife());
                            }
                            if (!"".equals(playerDetail.getDamageToHeroPerLife()) && playerDetail.getDamageToHeroPerLife() != null) {
                                datas[12][5] += Double.parseDouble(playerDetail.getDamageToHeroPerLife());
                            }
                            if (!"".equals(playerDetail.getDamageToShieldPerLife()) && playerDetail.getDamageToShieldPerLife() != null) {
                                datas[12][6] += Double.parseDouble(playerDetail.getDamageToShieldPerLife());
                            }
                            if (playerDetail.getIndex1().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex1()) && playerDetail.getIndex1() != null) {
                                    datas[12][7] += Double.parseDouble(playerDetail.getIndex1());
                                }
                            } else {
                                datas[12][7] += Double.parseDouble(playerDetail.getIndex1().replace("%", ""));
                            }
                            if (playerDetail.getIndex2().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex2()) && playerDetail.getIndex2() != null) {
                                    datas[12][8] += Double.parseDouble(playerDetail.getIndex2());
                                }
                            } else {
                                datas[12][8] += Double.parseDouble(playerDetail.getIndex2().replace("%", ""));
                            }
                            if (playerDetail.getIndex3().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex3()) && playerDetail.getIndex3() != null) {
                                    datas[12][9] += Double.parseDouble(playerDetail.getIndex3());
                                }
                            } else {
                                datas[12][9] += Double.parseDouble(playerDetail.getIndex3().replace("%", ""));
                            }
                            if (playerDetail.getIndex4().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex4()) && playerDetail.getIndex4() != null) {
                                    datas[12][10] += Double.parseDouble(playerDetail.getIndex4());
                                }
                            } else {
                                datas[12][10] += Double.parseDouble(playerDetail.getIndex4().replace("%", ""));
                            }
                            if (playerDetail.getIndex5().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex5()) && playerDetail.getIndex5() != null) {
                                    datas[12][11] += Double.parseDouble(playerDetail.getIndex5());
                                }
                            } else {
                                datas[12][11] += Double.parseDouble(playerDetail.getIndex5().replace("%", ""));
                            }
                            meiCnt ++ ;
                            break;
                        case "맥크리":
                            datas[13][0] += Double.parseDouble(playerDetail.getKillPerDeath());
                            datas[13][1] += Double.parseDouble(playerDetail.getDeathAvg());
                            if (!"".equals(playerDetail.getHealPerLife()) && playerDetail.getHealPerLife() != null) {
                                datas[13][2] += Double.parseDouble(playerDetail.getHealPerLife());
                            }
                            if (!"".equals(playerDetail.getBlockDamagePerLife()) && playerDetail.getBlockDamagePerLife() != null) {
                                datas[13][3] += Double.parseDouble(playerDetail.getBlockDamagePerLife());
                            }
                            if (!"".equals(playerDetail.getLastHitPerLife()) && playerDetail.getLastHitPerLife() != null) {
                                datas[13][4] += Double.parseDouble(playerDetail.getLastHitPerLife());
                            }
                            if (!"".equals(playerDetail.getDamageToHeroPerLife()) && playerDetail.getDamageToHeroPerLife() != null) {
                                datas[13][5] += Double.parseDouble(playerDetail.getDamageToHeroPerLife());
                            }
                            if (!"".equals(playerDetail.getDamageToShieldPerLife()) && playerDetail.getDamageToShieldPerLife() != null) {
                                datas[13][6] += Double.parseDouble(playerDetail.getDamageToShieldPerLife());
                            }
                            if (playerDetail.getIndex1().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex1()) && playerDetail.getIndex1() != null) {
                                    datas[13][7] += Double.parseDouble(playerDetail.getIndex1());
                                }
                            } else {
                                datas[13][7] += Double.parseDouble(playerDetail.getIndex1().replace("%", ""));
                            }
                            if (playerDetail.getIndex2().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex2()) && playerDetail.getIndex2() != null) {
                                    datas[13][8] += Double.parseDouble(playerDetail.getIndex2());
                                }
                            } else {
                                datas[13][8] += Double.parseDouble(playerDetail.getIndex2().replace("%", ""));
                            }
                            if (playerDetail.getIndex3().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex3()) && playerDetail.getIndex3() != null) {
                                    datas[13][9] += Double.parseDouble(playerDetail.getIndex3());
                                }
                            } else {
                                datas[13][9] += Double.parseDouble(playerDetail.getIndex3().replace("%", ""));
                            }
                            if (playerDetail.getIndex4().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex4()) && playerDetail.getIndex4() != null) {
                                    datas[13][10] += Double.parseDouble(playerDetail.getIndex4());
                                }
                            } else {
                                datas[13][10] += Double.parseDouble(playerDetail.getIndex4().replace("%", ""));
                            }
                            if (playerDetail.getIndex5().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex5()) && playerDetail.getIndex5() != null) {
                                    datas[13][11] += Double.parseDouble(playerDetail.getIndex5());
                                }
                            } else {
                                datas[13][11] += Double.parseDouble(playerDetail.getIndex5().replace("%", ""));
                            }
                            mccreeCnt ++ ;
                            break;
                        case "둠피스트":
                            datas[14][0] += Double.parseDouble(playerDetail.getKillPerDeath());
                            datas[14][1] += Double.parseDouble(playerDetail.getDeathAvg());
                            if (!"".equals(playerDetail.getHealPerLife()) && playerDetail.getHealPerLife() != null) {
                                datas[14][2] += Double.parseDouble(playerDetail.getHealPerLife());
                            }
                            if (!"".equals(playerDetail.getBlockDamagePerLife()) && playerDetail.getBlockDamagePerLife() != null) {
                                datas[14][3] += Double.parseDouble(playerDetail.getBlockDamagePerLife());
                            }
                            if (!"".equals(playerDetail.getLastHitPerLife()) && playerDetail.getLastHitPerLife() != null) {
                                datas[14][4] += Double.parseDouble(playerDetail.getLastHitPerLife());
                            }
                            if (!"".equals(playerDetail.getDamageToHeroPerLife()) && playerDetail.getDamageToHeroPerLife() != null) {
                                datas[14][5] += Double.parseDouble(playerDetail.getDamageToHeroPerLife());
                            }
                            if (!"".equals(playerDetail.getDamageToShieldPerLife()) && playerDetail.getDamageToShieldPerLife() != null) {
                                datas[14][6] += Double.parseDouble(playerDetail.getDamageToShieldPerLife());
                            }
                            if (playerDetail.getIndex1().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex1()) && playerDetail.getIndex1() != null) {
                                    datas[14][7] += Double.parseDouble(playerDetail.getIndex1());
                                }
                            } else {
                                datas[14][7] += Double.parseDouble(playerDetail.getIndex1().replace("%", ""));
                            }
                            if (playerDetail.getIndex2().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex2()) && playerDetail.getIndex2() != null) {
                                    datas[14][8] += Double.parseDouble(playerDetail.getIndex2());
                                }
                            } else {
                                datas[14][8] += Double.parseDouble(playerDetail.getIndex2().replace("%", ""));
                            }
                            if (playerDetail.getIndex3().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex3()) && playerDetail.getIndex3() != null) {
                                    datas[14][9] += Double.parseDouble(playerDetail.getIndex3());
                                }
                            } else {
                                datas[14][9] += Double.parseDouble(playerDetail.getIndex3().replace("%", ""));
                            }
                            if (playerDetail.getIndex4().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex4()) && playerDetail.getIndex4() != null) {
                                    datas[14][10] += Double.parseDouble(playerDetail.getIndex4());
                                }
                            } else {
                                datas[14][10] += Double.parseDouble(playerDetail.getIndex4().replace("%", ""));
                            }
                            if (playerDetail.getIndex5().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex5()) && playerDetail.getIndex5() != null) {
                                    datas[14][11] += Double.parseDouble(playerDetail.getIndex5());
                                }
                            } else {
                                datas[14][11] += Double.parseDouble(playerDetail.getIndex5().replace("%", ""));
                            }
                            doomfistCnt ++ ;
                            break;
                        case "겐지":
                            datas[15][0] += Double.parseDouble(playerDetail.getKillPerDeath());
                            datas[15][1] += Double.parseDouble(playerDetail.getDeathAvg());
                            if (!"".equals(playerDetail.getHealPerLife()) && playerDetail.getHealPerLife() != null) {
                                datas[15][2] += Double.parseDouble(playerDetail.getHealPerLife());
                            }
                            if (!"".equals(playerDetail.getBlockDamagePerLife()) && playerDetail.getBlockDamagePerLife() != null) {
                                datas[15][3] += Double.parseDouble(playerDetail.getBlockDamagePerLife());
                            }
                            if (!"".equals(playerDetail.getLastHitPerLife()) && playerDetail.getLastHitPerLife() != null) {
                                datas[15][4] += Double.parseDouble(playerDetail.getLastHitPerLife());
                            }
                            if (!"".equals(playerDetail.getDamageToHeroPerLife()) && playerDetail.getDamageToHeroPerLife() != null) {
                                datas[15][5] += Double.parseDouble(playerDetail.getDamageToHeroPerLife());
                            }
                            if (!"".equals(playerDetail.getDamageToShieldPerLife()) && playerDetail.getDamageToShieldPerLife() != null) {
                                datas[15][6] += Double.parseDouble(playerDetail.getDamageToShieldPerLife());
                            }
                            if (playerDetail.getIndex1().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex1()) && playerDetail.getIndex1() != null) {
                                    datas[15][7] += Double.parseDouble(playerDetail.getIndex1());
                                }
                            } else {
                                datas[15][7] += Double.parseDouble(playerDetail.getIndex1().replace("%", ""));
                            }
                            if (playerDetail.getIndex2().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex2()) && playerDetail.getIndex2() != null) {
                                    datas[15][8] += Double.parseDouble(playerDetail.getIndex2());
                                }
                            } else {
                                datas[15][8] += Double.parseDouble(playerDetail.getIndex2().replace("%", ""));
                            }
                            if (playerDetail.getIndex3().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex3()) && playerDetail.getIndex3() != null) {
                                    datas[15][9] += Double.parseDouble(playerDetail.getIndex3());
                                }
                            } else {
                                datas[15][9] += Double.parseDouble(playerDetail.getIndex3().replace("%", ""));
                            }
                            if (playerDetail.getIndex4().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex4()) && playerDetail.getIndex4() != null) {
                                    datas[15][10] += Double.parseDouble(playerDetail.getIndex4());
                                }
                            } else {
                                datas[15][10] += Double.parseDouble(playerDetail.getIndex4().replace("%", ""));
                            }
                            if (playerDetail.getIndex5().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex5()) && playerDetail.getIndex5() != null) {
                                    datas[15][11] += Double.parseDouble(playerDetail.getIndex5());
                                }
                            } else {
                                datas[15][11] += Double.parseDouble(playerDetail.getIndex5().replace("%", ""));
                            }
                            genjiCnt ++ ;
                            break;
                        default:
                            break;
                    }
                }
            }

            if(player.getTankRatingPoint() >= min && player.getTankRatingPoint() <= max) {
                for(PlayerDetail playerDetail : playerDetails) {
                    switch (playerDetail.getHeroNameKR()) {
                        case "오리사":
                            datas[16][0] += Double.parseDouble(playerDetail.getKillPerDeath());
                            datas[16][1] += Double.parseDouble(playerDetail.getDeathAvg());
                            if (!"".equals(playerDetail.getHealPerLife()) && playerDetail.getHealPerLife() != null) {
                                datas[16][2] += Double.parseDouble(playerDetail.getHealPerLife());
                            }
                            if (!"".equals(playerDetail.getBlockDamagePerLife()) && playerDetail.getBlockDamagePerLife() != null) {
                                datas[16][3] += Double.parseDouble(playerDetail.getBlockDamagePerLife());
                            }
                            if (!"".equals(playerDetail.getLastHitPerLife()) && playerDetail.getLastHitPerLife() != null) {
                                datas[16][4] += Double.parseDouble(playerDetail.getLastHitPerLife());
                            }
                            if (!"".equals(playerDetail.getDamageToHeroPerLife()) && playerDetail.getDamageToHeroPerLife() != null) {
                                datas[16][5] += Double.parseDouble(playerDetail.getDamageToHeroPerLife());
                            }
                            if (!"".equals(playerDetail.getDamageToShieldPerLife()) && playerDetail.getDamageToShieldPerLife() != null) {
                                datas[16][6] += Double.parseDouble(playerDetail.getDamageToShieldPerLife());
                            }
                            if (playerDetail.getIndex1().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex1()) && playerDetail.getIndex1() != null) {
                                    datas[16][7] += Double.parseDouble(playerDetail.getIndex1());
                                }
                            } else {
                                datas[16][7] += Double.parseDouble(playerDetail.getIndex1().replace("%", ""));
                            }
                            if (playerDetail.getIndex2().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex2()) && playerDetail.getIndex2() != null) {
                                    datas[16][8] += Double.parseDouble(playerDetail.getIndex2());
                                }
                            } else {
                                datas[16][8] += Double.parseDouble(playerDetail.getIndex2().replace("%", ""));
                            }
                            if (playerDetail.getIndex3().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex3()) && playerDetail.getIndex3() != null) {
                                    datas[16][9] += Double.parseDouble(playerDetail.getIndex3());
                                }
                            } else {
                                datas[16][9] += Double.parseDouble(playerDetail.getIndex3().replace("%", ""));
                            }
                            if (playerDetail.getIndex4().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex4()) && playerDetail.getIndex4() != null) {
                                    datas[16][10] += Double.parseDouble(playerDetail.getIndex4());
                                }
                            } else {
                                datas[16][10] += Double.parseDouble(playerDetail.getIndex4().replace("%", ""));
                            }
                            if (playerDetail.getIndex5().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex5()) && playerDetail.getIndex5() != null) {
                                    datas[16][11] += Double.parseDouble(playerDetail.getIndex5());
                                }
                            } else {
                                datas[16][11] += Double.parseDouble(playerDetail.getIndex5().replace("%", ""));
                            }
                            orisaCnt ++ ;
                            break;
                        case "시그마":
                            datas[17][0] += Double.parseDouble(playerDetail.getKillPerDeath());
                            datas[17][1] += Double.parseDouble(playerDetail.getDeathAvg());
                            if (!"".equals(playerDetail.getHealPerLife()) && playerDetail.getHealPerLife() != null) {
                                datas[17][2] += Double.parseDouble(playerDetail.getHealPerLife());
                            }
                            if (!"".equals(playerDetail.getBlockDamagePerLife()) && playerDetail.getBlockDamagePerLife() != null) {
                                datas[17][3] += Double.parseDouble(playerDetail.getBlockDamagePerLife());
                            }
                            if (!"".equals(playerDetail.getLastHitPerLife()) && playerDetail.getLastHitPerLife() != null) {
                                datas[17][4] += Double.parseDouble(playerDetail.getLastHitPerLife());
                            }
                            if (!"".equals(playerDetail.getDamageToHeroPerLife()) && playerDetail.getDamageToHeroPerLife() != null) {
                                datas[17][5] += Double.parseDouble(playerDetail.getDamageToHeroPerLife());
                            }
                            if (!"".equals(playerDetail.getDamageToShieldPerLife()) && playerDetail.getDamageToShieldPerLife() != null) {
                                datas[17][6] += Double.parseDouble(playerDetail.getDamageToShieldPerLife());
                            }
                            if (playerDetail.getIndex1().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex1()) && playerDetail.getIndex1() != null) {
                                    datas[17][7] += Double.parseDouble(playerDetail.getIndex1());
                                }
                            } else {
                                datas[17][7] += Double.parseDouble(playerDetail.getIndex1().replace("%", ""));
                            }
                            if (playerDetail.getIndex2().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex2()) && playerDetail.getIndex2() != null) {
                                    datas[17][8] += Double.parseDouble(playerDetail.getIndex2());
                                }
                            } else {
                                datas[17][8] += Double.parseDouble(playerDetail.getIndex2().replace("%", ""));
                            }
                            if (playerDetail.getIndex3().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex3()) && playerDetail.getIndex3() != null) {
                                    datas[17][9] += Double.parseDouble(playerDetail.getIndex3());
                                }
                            } else {
                                datas[17][9] += Double.parseDouble(playerDetail.getIndex3().replace("%", ""));
                            }
                            if (playerDetail.getIndex4().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex4()) && playerDetail.getIndex4() != null) {
                                    datas[17][10] += Double.parseDouble(playerDetail.getIndex4());
                                }
                            } else {
                                datas[17][10] += Double.parseDouble(playerDetail.getIndex4().replace("%", ""));
                            }
                            if (playerDetail.getIndex5().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex5()) && playerDetail.getIndex5() != null) {
                                    datas[17][11] += Double.parseDouble(playerDetail.getIndex5());
                                }
                            } else {
                                datas[17][11] += Double.parseDouble(playerDetail.getIndex5().replace("%", ""));
                            }
                            sigmaCnt ++ ;
                            break;
                        case "자리야":
                            datas[18][0] += Double.parseDouble(playerDetail.getKillPerDeath());
                            datas[18][1] += Double.parseDouble(playerDetail.getDeathAvg());
                            if (!"".equals(playerDetail.getHealPerLife()) && playerDetail.getHealPerLife() != null) {
                                datas[18][2] += Double.parseDouble(playerDetail.getHealPerLife());
                            }
                            if (!"".equals(playerDetail.getBlockDamagePerLife()) && playerDetail.getBlockDamagePerLife() != null) {
                                datas[18][3] += Double.parseDouble(playerDetail.getBlockDamagePerLife());
                            }
                            if (!"".equals(playerDetail.getLastHitPerLife()) && playerDetail.getLastHitPerLife() != null) {
                                datas[18][4] += Double.parseDouble(playerDetail.getLastHitPerLife());
                            }
                            if (!"".equals(playerDetail.getDamageToHeroPerLife()) && playerDetail.getDamageToHeroPerLife() != null) {
                                datas[18][5] += Double.parseDouble(playerDetail.getDamageToHeroPerLife());
                            }
                            if (!"".equals(playerDetail.getDamageToShieldPerLife()) && playerDetail.getDamageToShieldPerLife() != null) {
                                datas[18][6] += Double.parseDouble(playerDetail.getDamageToShieldPerLife());
                            }
                            if (playerDetail.getIndex1().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex1()) && playerDetail.getIndex1() != null) {
                                    datas[18][7] += Double.parseDouble(playerDetail.getIndex1());
                                }
                            } else {
                                datas[18][7] += Double.parseDouble(playerDetail.getIndex1().replace("%", ""));
                            }
                            if (playerDetail.getIndex2().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex2()) && playerDetail.getIndex2() != null) {
                                    datas[18][8] += Double.parseDouble(playerDetail.getIndex2());
                                }
                            } else {
                                datas[18][8] += Double.parseDouble(playerDetail.getIndex2().replace("%", ""));
                            }
                            if (playerDetail.getIndex3().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex3()) && playerDetail.getIndex3() != null) {
                                    datas[18][9] += Double.parseDouble(playerDetail.getIndex3());
                                }
                            } else {
                                datas[18][9] += Double.parseDouble(playerDetail.getIndex3().replace("%", ""));
                            }
                            if (playerDetail.getIndex4().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex4()) && playerDetail.getIndex4() != null) {
                                    datas[18][10] += Double.parseDouble(playerDetail.getIndex4());
                                }
                            } else {
                                datas[18][10] += Double.parseDouble(playerDetail.getIndex4().replace("%", ""));
                            }
                            if (playerDetail.getIndex5().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex5()) && playerDetail.getIndex5() != null) {
                                    datas[18][11] += Double.parseDouble(playerDetail.getIndex5());
                                }
                            } else {
                                datas[18][11] += Double.parseDouble(playerDetail.getIndex5().replace("%", ""));
                            }
                            zaryaCnt ++ ;
                            break;
                        case "라인하르트":
                            datas[19][0] += Double.parseDouble(playerDetail.getKillPerDeath());
                            datas[19][1] += Double.parseDouble(playerDetail.getDeathAvg());
                            if (!"".equals(playerDetail.getHealPerLife()) && playerDetail.getHealPerLife() != null) {
                                datas[19][2] += Double.parseDouble(playerDetail.getHealPerLife());
                            }
                            if (!"".equals(playerDetail.getBlockDamagePerLife()) && playerDetail.getBlockDamagePerLife() != null) {
                                datas[19][3] += Double.parseDouble(playerDetail.getBlockDamagePerLife());
                            }
                            if (!"".equals(playerDetail.getLastHitPerLife()) && playerDetail.getLastHitPerLife() != null) {
                                datas[19][4] += Double.parseDouble(playerDetail.getLastHitPerLife());
                            }
                            if (!"".equals(playerDetail.getDamageToHeroPerLife()) && playerDetail.getDamageToHeroPerLife() != null) {
                                datas[19][5] += Double.parseDouble(playerDetail.getDamageToHeroPerLife());
                            }
                            if (!"".equals(playerDetail.getDamageToShieldPerLife()) && playerDetail.getDamageToShieldPerLife() != null) {
                                datas[19][6] += Double.parseDouble(playerDetail.getDamageToShieldPerLife());
                            }
                            if (playerDetail.getIndex1().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex1()) && playerDetail.getIndex1() != null) {
                                    datas[19][7] += Double.parseDouble(playerDetail.getIndex1());
                                }
                            } else {
                                datas[19][7] += Double.parseDouble(playerDetail.getIndex1().replace("%", ""));
                            }
                            if (playerDetail.getIndex2().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex2()) && playerDetail.getIndex2() != null) {
                                    datas[19][8] += Double.parseDouble(playerDetail.getIndex2());
                                }
                            } else {
                                datas[19][8] += Double.parseDouble(playerDetail.getIndex2().replace("%", ""));
                            }
                            if (playerDetail.getIndex3().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex3()) && playerDetail.getIndex3() != null) {
                                    datas[19][9] += Double.parseDouble(playerDetail.getIndex3());
                                }
                            } else {
                                datas[19][9] += Double.parseDouble(playerDetail.getIndex3().replace("%", ""));
                            }
                            if (playerDetail.getIndex4().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex4()) && playerDetail.getIndex4() != null) {
                                    datas[19][10] += Double.parseDouble(playerDetail.getIndex4());
                                }
                            } else {
                                datas[19][10] += Double.parseDouble(playerDetail.getIndex4().replace("%", ""));
                            }
                            if (playerDetail.getIndex5().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex5()) && playerDetail.getIndex5() != null) {
                                    datas[19][11] += Double.parseDouble(playerDetail.getIndex5());
                                }
                            } else {
                                datas[19][11] += Double.parseDouble(playerDetail.getIndex5().replace("%", ""));
                            }
                            reinhardtCnt ++ ;
                            break;
                        case "디바":
                            datas[20][0] += Double.parseDouble(playerDetail.getKillPerDeath());
                            datas[20][1] += Double.parseDouble(playerDetail.getDeathAvg());
                            if (!"".equals(playerDetail.getHealPerLife()) && playerDetail.getHealPerLife() != null) {
                                datas[20][2] += Double.parseDouble(playerDetail.getHealPerLife());
                            }
                            if (!"".equals(playerDetail.getBlockDamagePerLife()) && playerDetail.getBlockDamagePerLife() != null) {
                                datas[20][3] += Double.parseDouble(playerDetail.getBlockDamagePerLife());
                            }
                            if (!"".equals(playerDetail.getLastHitPerLife()) && playerDetail.getLastHitPerLife() != null) {
                                datas[20][4] += Double.parseDouble(playerDetail.getLastHitPerLife());
                            }
                            if (!"".equals(playerDetail.getDamageToHeroPerLife()) && playerDetail.getDamageToHeroPerLife() != null) {
                                datas[20][5] += Double.parseDouble(playerDetail.getDamageToHeroPerLife());
                            }
                            if (!"".equals(playerDetail.getDamageToShieldPerLife()) && playerDetail.getDamageToShieldPerLife() != null) {
                                datas[20][6] += Double.parseDouble(playerDetail.getDamageToShieldPerLife());
                            }
                            if (playerDetail.getIndex1().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex1()) && playerDetail.getIndex1() != null) {
                                    datas[20][7] += Double.parseDouble(playerDetail.getIndex1());
                                }
                            } else {
                                datas[20][7] += Double.parseDouble(playerDetail.getIndex1().replace("%", ""));
                            }
                            if (playerDetail.getIndex2().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex2()) && playerDetail.getIndex2() != null) {
                                    datas[20][8] += Double.parseDouble(playerDetail.getIndex2());
                                }
                            } else {
                                datas[20][8] += Double.parseDouble(playerDetail.getIndex2().replace("%", ""));
                            }
                            if (playerDetail.getIndex3().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex3()) && playerDetail.getIndex3() != null) {
                                    datas[20][9] += Double.parseDouble(playerDetail.getIndex3());
                                }
                            } else {
                                datas[20][9] += Double.parseDouble(playerDetail.getIndex3().replace("%", ""));
                            }
                            if (playerDetail.getIndex4().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex4()) && playerDetail.getIndex4() != null) {
                                    datas[20][10] += Double.parseDouble(playerDetail.getIndex4());
                                }
                            } else {
                                datas[20][10] += Double.parseDouble(playerDetail.getIndex4().replace("%", ""));
                            }
                            if (playerDetail.getIndex5().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex5()) && playerDetail.getIndex5() != null) {
                                    datas[20][11] += Double.parseDouble(playerDetail.getIndex5());
                                }
                            } else {
                                datas[20][11] += Double.parseDouble(playerDetail.getIndex5().replace("%", ""));
                            }
                            dvaCnt ++ ;
                            break;
                        case "윈스턴":
                            datas[21][0] += Double.parseDouble(playerDetail.getKillPerDeath());
                            datas[21][1] += Double.parseDouble(playerDetail.getDeathAvg());
                            if (!"".equals(playerDetail.getHealPerLife()) && playerDetail.getHealPerLife() != null) {
                                datas[21][2] += Double.parseDouble(playerDetail.getHealPerLife());
                            }
                            if (!"".equals(playerDetail.getBlockDamagePerLife()) && playerDetail.getBlockDamagePerLife() != null) {
                                datas[21][3] += Double.parseDouble(playerDetail.getBlockDamagePerLife());
                            }
                            if (!"".equals(playerDetail.getLastHitPerLife()) && playerDetail.getLastHitPerLife() != null) {
                                datas[21][4] += Double.parseDouble(playerDetail.getLastHitPerLife());
                            }
                            if (!"".equals(playerDetail.getDamageToHeroPerLife()) && playerDetail.getDamageToHeroPerLife() != null) {
                                datas[21][5] += Double.parseDouble(playerDetail.getDamageToHeroPerLife());
                            }
                            if (!"".equals(playerDetail.getDamageToShieldPerLife()) && playerDetail.getDamageToShieldPerLife() != null) {
                                datas[21][6] += Double.parseDouble(playerDetail.getDamageToShieldPerLife());
                            }
                            if (playerDetail.getIndex1().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex1()) && playerDetail.getIndex1() != null) {
                                    datas[21][7] += Double.parseDouble(playerDetail.getIndex1());
                                }
                            } else {
                                datas[21][7] += Double.parseDouble(playerDetail.getIndex1().replace("%", ""));
                            }
                            if (playerDetail.getIndex2().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex2()) && playerDetail.getIndex2() != null) {
                                    datas[21][8] += Double.parseDouble(playerDetail.getIndex2());
                                }
                            } else {
                                datas[21][8] += Double.parseDouble(playerDetail.getIndex2().replace("%", ""));
                            }
                            if (playerDetail.getIndex3().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex3()) && playerDetail.getIndex3() != null) {
                                    datas[21][9] += Double.parseDouble(playerDetail.getIndex3());
                                }
                            } else {
                                datas[21][9] += Double.parseDouble(playerDetail.getIndex3().replace("%", ""));
                            }
                            if (playerDetail.getIndex4().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex4()) && playerDetail.getIndex4() != null) {
                                    datas[21][10] += Double.parseDouble(playerDetail.getIndex4());
                                }
                            } else {
                                datas[21][10] += Double.parseDouble(playerDetail.getIndex4().replace("%", ""));
                            }
                            if (playerDetail.getIndex5().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex5()) && playerDetail.getIndex5() != null) {
                                    datas[21][11] += Double.parseDouble(playerDetail.getIndex5());
                                }
                            } else {
                                datas[21][11] += Double.parseDouble(playerDetail.getIndex5().replace("%", ""));
                            }
                            winstonCnt ++ ;
                            break;
                        case "로드호그":
                            datas[22][0] += Double.parseDouble(playerDetail.getKillPerDeath());
                            datas[22][1] += Double.parseDouble(playerDetail.getDeathAvg());
                            if (!"".equals(playerDetail.getHealPerLife()) && playerDetail.getHealPerLife() != null) {
                                datas[22][2] += Double.parseDouble(playerDetail.getHealPerLife());
                            }
                            if (!"".equals(playerDetail.getBlockDamagePerLife()) && playerDetail.getBlockDamagePerLife() != null) {
                                datas[22][3] += Double.parseDouble(playerDetail.getBlockDamagePerLife());
                            }
                            if (!"".equals(playerDetail.getLastHitPerLife()) && playerDetail.getLastHitPerLife() != null) {
                                datas[22][4] += Double.parseDouble(playerDetail.getLastHitPerLife());
                            }
                            if (!"".equals(playerDetail.getDamageToHeroPerLife()) && playerDetail.getDamageToHeroPerLife() != null) {
                                datas[22][5] += Double.parseDouble(playerDetail.getDamageToHeroPerLife());
                            }
                            if (!"".equals(playerDetail.getDamageToShieldPerLife()) && playerDetail.getDamageToShieldPerLife() != null) {
                                datas[22][6] += Double.parseDouble(playerDetail.getDamageToShieldPerLife());
                            }
                            if (playerDetail.getIndex1().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex1()) && playerDetail.getIndex1() != null) {
                                    datas[22][7] += Double.parseDouble(playerDetail.getIndex1());
                                }
                            } else {
                                datas[22][7] += Double.parseDouble(playerDetail.getIndex1().replace("%", ""));
                            }
                            if (playerDetail.getIndex2().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex2()) && playerDetail.getIndex2() != null) {
                                    datas[22][8] += Double.parseDouble(playerDetail.getIndex2());
                                }
                            } else {
                                datas[22][8] += Double.parseDouble(playerDetail.getIndex2().replace("%", ""));
                            }
                            if (playerDetail.getIndex3().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex3()) && playerDetail.getIndex3() != null) {
                                    datas[22][9] += Double.parseDouble(playerDetail.getIndex3());
                                }
                            } else {
                                datas[22][9] += Double.parseDouble(playerDetail.getIndex3().replace("%", ""));
                            }
                            if (playerDetail.getIndex4().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex4()) && playerDetail.getIndex4() != null) {
                                    datas[22][10] += Double.parseDouble(playerDetail.getIndex4());
                                }
                            } else {
                                datas[22][10] += Double.parseDouble(playerDetail.getIndex4().replace("%", ""));
                            }
                            if (playerDetail.getIndex5().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex5()) && playerDetail.getIndex5() != null) {
                                    datas[22][11] += Double.parseDouble(playerDetail.getIndex5());
                                }
                            } else {
                                datas[22][11] += Double.parseDouble(playerDetail.getIndex5().replace("%", ""));
                            }
                            roadhogCnt ++ ;
                            break;
                        case "레킹볼":
                            datas[23][0] += Double.parseDouble(playerDetail.getKillPerDeath());
                            datas[23][1] += Double.parseDouble(playerDetail.getDeathAvg());
                            if (!"".equals(playerDetail.getHealPerLife()) && playerDetail.getHealPerLife() != null) {
                                datas[23][2] += Double.parseDouble(playerDetail.getHealPerLife());
                            }
                            if (!"".equals(playerDetail.getBlockDamagePerLife()) && playerDetail.getBlockDamagePerLife() != null) {
                                datas[23][3] += Double.parseDouble(playerDetail.getBlockDamagePerLife());
                            }
                            if (!"".equals(playerDetail.getLastHitPerLife()) && playerDetail.getLastHitPerLife() != null) {
                                datas[23][4] += Double.parseDouble(playerDetail.getLastHitPerLife());
                            }
                            if (!"".equals(playerDetail.getDamageToHeroPerLife()) && playerDetail.getDamageToHeroPerLife() != null) {
                                datas[23][5] += Double.parseDouble(playerDetail.getDamageToHeroPerLife());
                            }
                            if (!"".equals(playerDetail.getDamageToShieldPerLife()) && playerDetail.getDamageToShieldPerLife() != null) {
                                datas[23][6] += Double.parseDouble(playerDetail.getDamageToShieldPerLife());
                            }
                            if (playerDetail.getIndex1().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex1()) && playerDetail.getIndex1() != null) {
                                    datas[23][7] += Double.parseDouble(playerDetail.getIndex1());
                                }
                            } else {
                                datas[23][7] += Double.parseDouble(playerDetail.getIndex1().replace("%", ""));
                            }
                            if (playerDetail.getIndex2().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex2()) && playerDetail.getIndex2() != null) {
                                    datas[23][8] += Double.parseDouble(playerDetail.getIndex2());
                                }
                            } else {
                                datas[23][8] += Double.parseDouble(playerDetail.getIndex2().replace("%", ""));
                            }
                            if (playerDetail.getIndex3().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex3()) && playerDetail.getIndex3() != null) {
                                    datas[23][9] += Double.parseDouble(playerDetail.getIndex3());
                                }
                            } else {
                                datas[23][9] += Double.parseDouble(playerDetail.getIndex3().replace("%", ""));
                            }
                            if (playerDetail.getIndex4().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex4()) && playerDetail.getIndex4() != null) {
                                    datas[23][10] += Double.parseDouble(playerDetail.getIndex4());
                                }
                            } else {
                                datas[23][10] += Double.parseDouble(playerDetail.getIndex4().replace("%", ""));
                            }
                            if (playerDetail.getIndex5().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex5()) && playerDetail.getIndex5() != null) {
                                    datas[23][11] += Double.parseDouble(playerDetail.getIndex5());
                                }
                            } else {
                                datas[23][11] += Double.parseDouble(playerDetail.getIndex5().replace("%", ""));
                            }
                            wreckingballCnt ++ ;
                            break;
                        default:
                            break;
                    }
                }
            }

            if(player.getHealRatingPoint() >= min && player.getHealRatingPoint() <= max) {
                for(PlayerDetail playerDetail : playerDetails) {
                    switch (playerDetail.getHeroNameKR()) {
                        case "모이라":
                            datas[24][0] += Double.parseDouble(playerDetail.getKillPerDeath());
                            datas[24][1] += Double.parseDouble(playerDetail.getDeathAvg());
                            if (!"".equals(playerDetail.getHealPerLife()) && playerDetail.getHealPerLife() != null) {
                                datas[24][2] += Double.parseDouble(playerDetail.getHealPerLife());
                            }
                            if (!"".equals(playerDetail.getBlockDamagePerLife()) && playerDetail.getBlockDamagePerLife() != null) {
                                datas[24][3] += Double.parseDouble(playerDetail.getBlockDamagePerLife());
                            }
                            if (!"".equals(playerDetail.getLastHitPerLife()) && playerDetail.getLastHitPerLife() != null) {
                                datas[24][4] += Double.parseDouble(playerDetail.getLastHitPerLife());
                            }
                            if (!"".equals(playerDetail.getDamageToHeroPerLife()) && playerDetail.getDamageToHeroPerLife() != null) {
                                datas[24][5] += Double.parseDouble(playerDetail.getDamageToHeroPerLife());
                            }
                            if (!"".equals(playerDetail.getDamageToShieldPerLife()) && playerDetail.getDamageToShieldPerLife() != null) {
                                datas[24][6] += Double.parseDouble(playerDetail.getDamageToShieldPerLife());
                            }
                            if (playerDetail.getIndex1().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex1()) && playerDetail.getIndex1() != null) {
                                    datas[24][7] += Double.parseDouble(playerDetail.getIndex1());
                                }
                            } else {
                                datas[24][7] += Double.parseDouble(playerDetail.getIndex1().replace("%", ""));
                            }
                            if (playerDetail.getIndex2().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex2()) && playerDetail.getIndex2() != null) {
                                    datas[24][8] += Double.parseDouble(playerDetail.getIndex2());
                                }
                            } else {
                                datas[24][8] += Double.parseDouble(playerDetail.getIndex2().replace("%", ""));
                            }
                            if (playerDetail.getIndex3().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex3()) && playerDetail.getIndex3() != null) {
                                    datas[24][9] += Double.parseDouble(playerDetail.getIndex3());
                                }
                            } else {
                                datas[24][9] += Double.parseDouble(playerDetail.getIndex3().replace("%", ""));
                            }
                            if (playerDetail.getIndex4().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex4()) && playerDetail.getIndex4() != null) {
                                    datas[24][10] += Double.parseDouble(playerDetail.getIndex4());
                                }
                            } else {
                                datas[24][10] += Double.parseDouble(playerDetail.getIndex4().replace("%", ""));
                            }
                            if (playerDetail.getIndex5().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex5()) && playerDetail.getIndex5() != null) {
                                    datas[24][11] += Double.parseDouble(playerDetail.getIndex5());
                                }
                            } else {
                                datas[24][11] += Double.parseDouble(playerDetail.getIndex5().replace("%", ""));
                            }
                            moiraCnt ++ ;
                            break;
                        case "아나":
                            datas[25][0] += Double.parseDouble(playerDetail.getKillPerDeath());
                            datas[25][1] += Double.parseDouble(playerDetail.getDeathAvg());
                            if (!"".equals(playerDetail.getHealPerLife()) && playerDetail.getHealPerLife() != null) {
                                datas[25][2] += Double.parseDouble(playerDetail.getHealPerLife());
                            }
                            if (!"".equals(playerDetail.getBlockDamagePerLife()) && playerDetail.getBlockDamagePerLife() != null) {
                                datas[25][3] += Double.parseDouble(playerDetail.getBlockDamagePerLife());
                            }
                            if (!"".equals(playerDetail.getLastHitPerLife()) && playerDetail.getLastHitPerLife() != null) {
                                datas[25][4] += Double.parseDouble(playerDetail.getLastHitPerLife());
                            }
                            if (!"".equals(playerDetail.getDamageToHeroPerLife()) && playerDetail.getDamageToHeroPerLife() != null) {
                                datas[25][5] += Double.parseDouble(playerDetail.getDamageToHeroPerLife());
                            }
                            if (!"".equals(playerDetail.getDamageToShieldPerLife()) && playerDetail.getDamageToShieldPerLife() != null) {
                                datas[25][6] += Double.parseDouble(playerDetail.getDamageToShieldPerLife());
                            }
                            if (playerDetail.getIndex1().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex1()) && playerDetail.getIndex1() != null) {
                                    datas[25][7] += Double.parseDouble(playerDetail.getIndex1());
                                }
                            } else {
                                datas[25][7] += Double.parseDouble(playerDetail.getIndex1().replace("%", ""));
                            }
                            if (playerDetail.getIndex2().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex2()) && playerDetail.getIndex2() != null) {
                                    datas[25][8] += Double.parseDouble(playerDetail.getIndex2());
                                }
                            } else {
                                datas[25][8] += Double.parseDouble(playerDetail.getIndex2().replace("%", ""));
                            }
                            if (playerDetail.getIndex3().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex3()) && playerDetail.getIndex3() != null) {
                                    datas[25][9] += Double.parseDouble(playerDetail.getIndex3());
                                }
                            } else {
                                datas[25][9] += Double.parseDouble(playerDetail.getIndex3().replace("%", ""));
                            }
                            if (playerDetail.getIndex4().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex4()) && playerDetail.getIndex4() != null) {
                                    datas[25][10] += Double.parseDouble(playerDetail.getIndex4());
                                }
                            } else {
                                datas[25][10] += Double.parseDouble(playerDetail.getIndex4().replace("%", ""));
                            }
                            if (playerDetail.getIndex5().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex5()) && playerDetail.getIndex5() != null) {
                                    datas[25][11] += Double.parseDouble(playerDetail.getIndex5());
                                }
                            } else {
                                datas[25][11] += Double.parseDouble(playerDetail.getIndex5().replace("%", ""));
                            }
                            anaCnt ++ ;
                            break;
                        case "브리기테":
                            datas[26][0] += Double.parseDouble(playerDetail.getKillPerDeath());
                            datas[26][1] += Double.parseDouble(playerDetail.getDeathAvg());
                            if (!"".equals(playerDetail.getHealPerLife()) && playerDetail.getHealPerLife() != null) {
                                datas[26][2] += Double.parseDouble(playerDetail.getHealPerLife());
                            }
                            if (!"".equals(playerDetail.getBlockDamagePerLife()) && playerDetail.getBlockDamagePerLife() != null) {
                                datas[26][3] += Double.parseDouble(playerDetail.getBlockDamagePerLife());
                            }
                            if (!"".equals(playerDetail.getLastHitPerLife()) && playerDetail.getLastHitPerLife() != null) {
                                datas[26][4] += Double.parseDouble(playerDetail.getLastHitPerLife());
                            }
                            if (!"".equals(playerDetail.getDamageToHeroPerLife()) && playerDetail.getDamageToHeroPerLife() != null) {
                                datas[26][5] += Double.parseDouble(playerDetail.getDamageToHeroPerLife());
                            }
                            if (!"".equals(playerDetail.getDamageToShieldPerLife()) && playerDetail.getDamageToShieldPerLife() != null) {
                                datas[26][6] += Double.parseDouble(playerDetail.getDamageToShieldPerLife());
                            }
                            if (playerDetail.getIndex1().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex1()) && playerDetail.getIndex1() != null) {
                                    datas[26][7] += Double.parseDouble(playerDetail.getIndex1());
                                }
                            } else {
                                datas[26][7] += Double.parseDouble(playerDetail.getIndex1().replace("%", ""));
                            }
                            if (playerDetail.getIndex2().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex2()) && playerDetail.getIndex2() != null) {
                                    datas[26][8] += Double.parseDouble(playerDetail.getIndex2());
                                }
                            } else {
                                datas[26][8] += Double.parseDouble(playerDetail.getIndex2().replace("%", ""));
                            }
                            if (playerDetail.getIndex3().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex3()) && playerDetail.getIndex3() != null) {
                                    datas[26][9] += Double.parseDouble(playerDetail.getIndex3());
                                }
                            } else {
                                datas[26][9] += Double.parseDouble(playerDetail.getIndex3().replace("%", ""));
                            }
                            if (playerDetail.getIndex4().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex4()) && playerDetail.getIndex4() != null) {
                                    datas[26][10] += Double.parseDouble(playerDetail.getIndex4());
                                }
                            } else {
                                datas[26][10] += Double.parseDouble(playerDetail.getIndex4().replace("%", ""));
                            }
                            if (playerDetail.getIndex5().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex5()) && playerDetail.getIndex5() != null) {
                                    datas[26][11] += Double.parseDouble(playerDetail.getIndex5());
                                }
                            } else {
                                datas[26][11] += Double.parseDouble(playerDetail.getIndex5().replace("%", ""));
                            }
                            brigitteCnt ++ ;
                            break;
                        case "바티스트":
                            datas[27][0] += Double.parseDouble(playerDetail.getKillPerDeath());
                            datas[27][1] += Double.parseDouble(playerDetail.getDeathAvg());
                            if (!"".equals(playerDetail.getHealPerLife()) && playerDetail.getHealPerLife() != null) {
                                datas[27][2] += Double.parseDouble(playerDetail.getHealPerLife());
                            }
                            if (!"".equals(playerDetail.getBlockDamagePerLife()) && playerDetail.getBlockDamagePerLife() != null) {
                                datas[27][3] += Double.parseDouble(playerDetail.getBlockDamagePerLife());
                            }
                            if (!"".equals(playerDetail.getLastHitPerLife()) && playerDetail.getLastHitPerLife() != null) {
                                datas[27][4] += Double.parseDouble(playerDetail.getLastHitPerLife());
                            }
                            if (!"".equals(playerDetail.getDamageToHeroPerLife()) && playerDetail.getDamageToHeroPerLife() != null) {
                                datas[27][5] += Double.parseDouble(playerDetail.getDamageToHeroPerLife());
                            }
                            if (!"".equals(playerDetail.getDamageToShieldPerLife()) && playerDetail.getDamageToShieldPerLife() != null) {
                                datas[27][6] += Double.parseDouble(playerDetail.getDamageToShieldPerLife());
                            }
                            if (playerDetail.getIndex1().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex1()) && playerDetail.getIndex1() != null) {
                                    datas[27][7] += Double.parseDouble(playerDetail.getIndex1());
                                }
                            } else {
                                datas[27][7] += Double.parseDouble(playerDetail.getIndex1().replace("%", ""));
                            }
                            if (playerDetail.getIndex2().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex2()) && playerDetail.getIndex2() != null) {
                                    datas[27][8] += Double.parseDouble(playerDetail.getIndex2());
                                }
                            } else {
                                datas[27][8] += Double.parseDouble(playerDetail.getIndex2().replace("%", ""));
                            }
                            if (playerDetail.getIndex3().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex3()) && playerDetail.getIndex3() != null) {
                                    datas[27][9] += Double.parseDouble(playerDetail.getIndex3());
                                }
                            } else {
                                datas[27][9] += Double.parseDouble(playerDetail.getIndex3().replace("%", ""));
                            }
                            if (playerDetail.getIndex4().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex4()) && playerDetail.getIndex4() != null) {
                                    datas[27][10] += Double.parseDouble(playerDetail.getIndex4());
                                }
                            } else {
                                datas[27][10] += Double.parseDouble(playerDetail.getIndex4().replace("%", ""));
                            }
                            if (playerDetail.getIndex5().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex5()) && playerDetail.getIndex5() != null) {
                                    datas[27][11] += Double.parseDouble(playerDetail.getIndex5());
                                }
                            } else {
                                datas[27][11] += Double.parseDouble(playerDetail.getIndex5().replace("%", ""));
                            }
                            baptisteCnt ++ ;
                            break;
                        case "젠야타":
                            datas[28][0] += Double.parseDouble(playerDetail.getKillPerDeath());
                            datas[28][1] += Double.parseDouble(playerDetail.getDeathAvg());
                            if (!"".equals(playerDetail.getHealPerLife()) && playerDetail.getHealPerLife() != null) {
                                datas[28][2] += Double.parseDouble(playerDetail.getHealPerLife());
                            }
                            if (!"".equals(playerDetail.getBlockDamagePerLife()) && playerDetail.getBlockDamagePerLife() != null) {
                                datas[28][3] += Double.parseDouble(playerDetail.getBlockDamagePerLife());
                            }
                            if (!"".equals(playerDetail.getLastHitPerLife()) && playerDetail.getLastHitPerLife() != null) {
                                datas[28][4] += Double.parseDouble(playerDetail.getLastHitPerLife());
                            }
                            if (!"".equals(playerDetail.getDamageToHeroPerLife()) && playerDetail.getDamageToHeroPerLife() != null) {
                                datas[28][5] += Double.parseDouble(playerDetail.getDamageToHeroPerLife());
                            }
                            if (!"".equals(playerDetail.getDamageToShieldPerLife()) && playerDetail.getDamageToShieldPerLife() != null) {
                                datas[28][6] += Double.parseDouble(playerDetail.getDamageToShieldPerLife());
                            }
                            if (playerDetail.getIndex1().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex1()) && playerDetail.getIndex1() != null) {
                                    datas[28][7] += Double.parseDouble(playerDetail.getIndex1());
                                }
                            } else {
                                datas[28][7] += Double.parseDouble(playerDetail.getIndex1().replace("%", ""));
                            }
                            if (playerDetail.getIndex2().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex2()) && playerDetail.getIndex2() != null) {
                                    datas[28][8] += Double.parseDouble(playerDetail.getIndex2());
                                }
                            } else {
                                datas[28][8] += Double.parseDouble(playerDetail.getIndex2().replace("%", ""));
                            }
                            if (playerDetail.getIndex3().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex3()) && playerDetail.getIndex3() != null) {
                                    datas[28][9] += Double.parseDouble(playerDetail.getIndex3());
                                }
                            } else {
                                datas[28][9] += Double.parseDouble(playerDetail.getIndex3().replace("%", ""));
                            }
                            if (playerDetail.getIndex4().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex4()) && playerDetail.getIndex4() != null) {
                                    datas[28][10] += Double.parseDouble(playerDetail.getIndex4());
                                }
                            } else {
                                datas[28][10] += Double.parseDouble(playerDetail.getIndex4().replace("%", ""));
                            }
                            if (playerDetail.getIndex5().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex5()) && playerDetail.getIndex5() != null) {
                                    datas[28][11] += Double.parseDouble(playerDetail.getIndex5());
                                }
                            } else {
                                datas[28][11] += Double.parseDouble(playerDetail.getIndex5().replace("%", ""));
                            }
                            zenyattaCnt ++ ;
                            break;
                        case "루시우":
                            datas[29][0] += Double.parseDouble(playerDetail.getKillPerDeath());
                            datas[29][1] += Double.parseDouble(playerDetail.getDeathAvg());
                            if (!"".equals(playerDetail.getHealPerLife()) && playerDetail.getHealPerLife() != null) {
                                datas[29][2] += Double.parseDouble(playerDetail.getHealPerLife());
                            }
                            if (!"".equals(playerDetail.getBlockDamagePerLife()) && playerDetail.getBlockDamagePerLife() != null) {
                                datas[29][3] += Double.parseDouble(playerDetail.getBlockDamagePerLife());
                            }
                            if (!"".equals(playerDetail.getLastHitPerLife()) && playerDetail.getLastHitPerLife() != null) {
                                datas[29][4] += Double.parseDouble(playerDetail.getLastHitPerLife());
                            }
                            if (!"".equals(playerDetail.getDamageToHeroPerLife()) && playerDetail.getDamageToHeroPerLife() != null) {
                                datas[29][5] += Double.parseDouble(playerDetail.getDamageToHeroPerLife());
                            }
                            if (!"".equals(playerDetail.getDamageToShieldPerLife()) && playerDetail.getDamageToShieldPerLife() != null) {
                                datas[29][6] += Double.parseDouble(playerDetail.getDamageToShieldPerLife());
                            }
                            if (playerDetail.getIndex1().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex1()) && playerDetail.getIndex1() != null) {
                                    datas[29][7] += Double.parseDouble(playerDetail.getIndex1());
                                }
                            } else {
                                datas[29][7] += Double.parseDouble(playerDetail.getIndex1().replace("%", ""));
                            }
                            if (playerDetail.getIndex2().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex2()) && playerDetail.getIndex2() != null) {
                                    datas[29][8] += Double.parseDouble(playerDetail.getIndex2());
                                }
                            } else {
                                datas[29][8] += Double.parseDouble(playerDetail.getIndex2().replace("%", ""));
                            }
                            if (playerDetail.getIndex3().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex3()) && playerDetail.getIndex3() != null) {
                                    datas[29][9] += Double.parseDouble(playerDetail.getIndex3());
                                }
                            } else {
                                datas[29][9] += Double.parseDouble(playerDetail.getIndex3().replace("%", ""));
                            }
                            if (playerDetail.getIndex4().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex4()) && playerDetail.getIndex4() != null) {
                                    datas[29][10] += Double.parseDouble(playerDetail.getIndex4());
                                }
                            } else {
                                datas[29][10] += Double.parseDouble(playerDetail.getIndex4().replace("%", ""));
                            }
                            if (playerDetail.getIndex5().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex5()) && playerDetail.getIndex5() != null) {
                                    datas[29][11] += Double.parseDouble(playerDetail.getIndex5());
                                }
                            } else {
                                datas[29][11] += Double.parseDouble(playerDetail.getIndex5().replace("%", ""));
                            }
                            lucioCnt ++ ;
                            break;
                        case "메르시":
                            datas[30][0] += Double.parseDouble(playerDetail.getKillPerDeath());
                            datas[30][1] += Double.parseDouble(playerDetail.getDeathAvg());
                            if (!"".equals(playerDetail.getHealPerLife()) && playerDetail.getHealPerLife() != null) {
                                datas[30][2] += Double.parseDouble(playerDetail.getHealPerLife());
                            }
                            if (!"".equals(playerDetail.getBlockDamagePerLife()) && playerDetail.getBlockDamagePerLife() != null) {
                                datas[30][3] += Double.parseDouble(playerDetail.getBlockDamagePerLife());
                            }
                            if (!"".equals(playerDetail.getLastHitPerLife()) && playerDetail.getLastHitPerLife() != null) {
                                datas[30][4] += Double.parseDouble(playerDetail.getLastHitPerLife());
                            }
                            if (!"".equals(playerDetail.getDamageToHeroPerLife()) && playerDetail.getDamageToHeroPerLife() != null) {
                                datas[30][5] += Double.parseDouble(playerDetail.getDamageToHeroPerLife());
                            }
                            if (!"".equals(playerDetail.getDamageToShieldPerLife()) && playerDetail.getDamageToShieldPerLife() != null) {
                                datas[30][6] += Double.parseDouble(playerDetail.getDamageToShieldPerLife());
                            }
                            if (playerDetail.getIndex1().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex1()) && playerDetail.getIndex1() != null) {
                                    datas[30][7] += Double.parseDouble(playerDetail.getIndex1());
                                }
                            } else {
                                datas[30][7] += Double.parseDouble(playerDetail.getIndex1().replace("%", ""));
                            }
                            if (playerDetail.getIndex2().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex2()) && playerDetail.getIndex2() != null) {
                                    datas[30][8] += Double.parseDouble(playerDetail.getIndex2());
                                }
                            } else {
                                datas[30][8] += Double.parseDouble(playerDetail.getIndex2().replace("%", ""));
                            }
                            if (playerDetail.getIndex3().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex3()) && playerDetail.getIndex3() != null) {
                                    datas[30][9] += Double.parseDouble(playerDetail.getIndex3());
                                }
                            } else {
                                datas[30][9] += Double.parseDouble(playerDetail.getIndex3().replace("%", ""));
                            }
                            if (playerDetail.getIndex4().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex4()) && playerDetail.getIndex4() != null) {
                                    datas[30][10] += Double.parseDouble(playerDetail.getIndex4());
                                }
                            } else {
                                datas[30][10] += Double.parseDouble(playerDetail.getIndex4().replace("%", ""));
                            }
                            if (playerDetail.getIndex5().indexOf("%") == -1) {
                                if(!"".equals(playerDetail.getIndex5()) && playerDetail.getIndex5() != null) {
                                    datas[30][11] += Double.parseDouble(playerDetail.getIndex5());
                                }
                            } else {
                                datas[30][11] += Double.parseDouble(playerDetail.getIndex5().replace("%", ""));
                            }
                            mercyCnt ++ ;
                            break;
                    }
                }
            }
        }
        log.debug("{} >>>>>>> process 진행중 | ======== 솔져 데이터 계산 시작 ==========", JOB_NAME);
        PlayerDetail soldier76Detail =  new PlayerDetail((long)min, season, 0, "soldier-76", "솔저: 76",
                calculateData(datas[0][0], soldier76Cnt), "", "", calculateData(datas[0][1], soldier76Cnt),
                "", calculateData(datas[0][2],soldier76Cnt), calculateData(datas[0][3],soldier76Cnt), calculateData(datas[0][4],soldier76Cnt),
                calculateData(datas[0][5],soldier76Cnt), calculateData(datas[0][6],soldier76Cnt), calculateData(datas[0][7],soldier76Cnt),
                calculateData(datas[0][8],soldier76Cnt), calculateData(datas[0][9],soldier76Cnt), calculateData(datas[0][10],soldier76Cnt),
                calculateData(datas[0][11],soldier76Cnt), "평균 나선 로켓 처치", "평균 전술조준경 처치", "치명타 명중률", "평균 단독처치", "");

        playerDetailList.add(soldier76Detail);

        log.debug("{} >>>>>>> process 진행중 | ======== 리퍼 데이터 계산 시작 ==========", JOB_NAME);
        PlayerDetail reaperDetail = new PlayerDetail((long)min, season, 1, "reaper", "리퍼",
                calculateData(datas[1][0], reaperCnt), "", "", calculateData(datas[1][1], reaperCnt),
                "", calculateData(datas[1][2],reaperCnt), calculateData(datas[1][3],reaperCnt), calculateData(datas[1][4],reaperCnt),
                calculateData(datas[1][5],reaperCnt), calculateData(datas[1][6],reaperCnt), calculateData(datas[1][7],reaperCnt),
                calculateData(datas[1][8],reaperCnt), calculateData(datas[1][9],reaperCnt), calculateData(datas[1][10],reaperCnt),
                calculateData(datas[1][11],reaperCnt), "평균 죽음의꽃 처치", "평균 단독처치", "", "", "");

        playerDetailList.add(reaperDetail);

        log.debug("{} >>>>>>> process 진행중 | ======== 정크랫 데이터 계산 시작 ==========", JOB_NAME);
        PlayerDetail junkratDetail = new PlayerDetail((long)min, season, 2, "junkrat", "정크랫",
                calculateData(datas[2][0], junkratCnt), "", "", calculateData(datas[2][1], junkratCnt),
                "", calculateData(datas[2][2],junkratCnt), calculateData(datas[2][3],junkratCnt), calculateData(datas[2][4],junkratCnt),
                calculateData(datas[2][5],junkratCnt), calculateData(datas[2][6],junkratCnt), calculateData(datas[2][7],junkratCnt),
                calculateData(datas[2][8],junkratCnt), calculateData(datas[2][9],junkratCnt), calculateData(datas[2][10],junkratCnt),
                calculateData(datas[2][11],junkratCnt), "평균 덫으로 묶은 적", "평균 충격 지뢰 처치", "평균 죽이는 타이어 처치", "평균 단독처치", "");

        playerDetailList.add(junkratDetail);

        log.debug("{} >>>>>>> process 진행중 | ======== 위도우메이커 데이터 계산 시작 ==========", JOB_NAME);
        PlayerDetail widowmakerDetail = new PlayerDetail((long)min, season, 3, "widowmaker", "위도우메이커",
                calculateData(datas[3][0], widowmakerCnt), "", "", calculateData(datas[3][1], widowmakerCnt),
                "", calculateData(datas[3][2],widowmakerCnt), calculateData(datas[3][3],widowmakerCnt), calculateData(datas[3][4],widowmakerCnt),
                calculateData(datas[3][5],widowmakerCnt), calculateData(datas[3][6],widowmakerCnt), calculateData(datas[3][7],widowmakerCnt),
                calculateData(datas[3][8],widowmakerCnt), calculateData(datas[3][9],widowmakerCnt), calculateData(datas[3][10],widowmakerCnt),
                calculateData(datas[3][11],widowmakerCnt), "평균 처치시야 지원", "저격 명중률", "저격 치명타 명중률", "평균 단독처치", "");

        playerDetailList.add(widowmakerDetail);

        log.debug("{} >>>>>>> process 진행중 | ======== 토르비욘 데이터 계산 시작 ==========", JOB_NAME);
        PlayerDetail torbjornDetail = new PlayerDetail((long)min, season, 4, "torbjorn", "토르비욘",
                calculateData(datas[4][0], torbjornCnt), "", "", calculateData(datas[4][1], torbjornCnt),
                "", calculateData(datas[4][2],torbjornCnt), calculateData(datas[4][3],torbjornCnt), calculateData(datas[4][4],torbjornCnt),
                calculateData(datas[4][5],torbjornCnt), calculateData(datas[4][6],torbjornCnt), calculateData(datas[4][7],torbjornCnt),
                calculateData(datas[4][8],torbjornCnt), calculateData(datas[4][9],torbjornCnt), calculateData(datas[4][10],torbjornCnt),
                calculateData(datas[4][11],torbjornCnt), "평균 초고열 용광로 처치", "평균 직접 처치", "평균 포탑 처치", "평균 단독처치", "");

        playerDetailList.add(torbjornDetail);

        log.debug("{} >>>>>>> process 진행중 | ======== 트레이서 데이터 계산 시작 ==========", JOB_NAME);
        PlayerDetail tracerDetail = new PlayerDetail((long)min, season, 5, "tracer", "트레이서",
                calculateData(datas[5][0], tracerCnt), "", "", calculateData(datas[5][1], tracerCnt),
                "", calculateData(datas[5][2],tracerCnt), calculateData(datas[5][3],tracerCnt), calculateData(datas[5][4],tracerCnt),
                calculateData(datas[5][5],tracerCnt), calculateData(datas[5][6],tracerCnt), calculateData(datas[5][7],tracerCnt),
                calculateData(datas[5][8],tracerCnt), calculateData(datas[5][9],tracerCnt), calculateData(datas[5][10],tracerCnt),
                calculateData(datas[5][11],tracerCnt), "평균 펄스폭탄 부착", "평균 펄스폭탄 처치", "치명타 명중률", "평균 단독처치", "");

        playerDetailList.add(tracerDetail);

        PlayerDetail pharahDetail = new PlayerDetail((long)min, season, 6, "pharah", "파라",
                calculateData(datas[6][0], pharahCnt), "", "", calculateData(datas[6][1], pharahCnt),
                "", calculateData(datas[6][2],pharahCnt), calculateData(datas[6][3],pharahCnt), calculateData(datas[6][4],pharahCnt),
                calculateData(datas[6][5],pharahCnt), calculateData(datas[6][6],pharahCnt), calculateData(datas[6][7],pharahCnt),
                calculateData(datas[6][8],pharahCnt), calculateData(datas[6][9],pharahCnt), calculateData(datas[6][10],pharahCnt),
                calculateData(datas[6][11],pharahCnt), "평균 로켓 명중", "직격률", "평균 포화 처치", "평균 단독처치", "");

        playerDetailList.add(pharahDetail);

        log.debug("{} >>>>>>> process 진행중 | ======== 한조 데이터 계산 시작 ==========", JOB_NAME);
        PlayerDetail hanzoDetail = new PlayerDetail((long)min, season, 7, "hanzo", "한조",
                calculateData(datas[7][0], hanzoCnt), "", "", calculateData(datas[7][1], hanzoCnt),
                "", calculateData(datas[7][2],hanzoCnt), calculateData(datas[7][3],hanzoCnt), calculateData(datas[7][4],hanzoCnt),
                calculateData(datas[7][5],hanzoCnt), calculateData(datas[7][6],hanzoCnt), calculateData(datas[7][7],hanzoCnt),
                calculateData(datas[7][8],hanzoCnt), calculateData(datas[7][9],hanzoCnt), calculateData(datas[7][10],hanzoCnt),
                calculateData(datas[7][11],hanzoCnt), "평균 용의 일격 처치", "평균 폭풍 화살 처치", "평균 처치시야 지원", "평균 단독처치", "");

        playerDetailList.add(hanzoDetail);

        log.debug("{} >>>>>>> process 진행중 | ======== 애쉬 데이터 계산 시작 ==========", JOB_NAME);
        PlayerDetail asheDetail = new PlayerDetail((long)min, season, 8, "ashe", "애쉬",
                calculateData(datas[8][0], asheCnt), "", "", calculateData(datas[8][1], asheCnt),
                "", calculateData(datas[8][2],asheCnt), calculateData(datas[8][3],asheCnt), calculateData(datas[8][4],asheCnt),
                calculateData(datas[8][5],asheCnt), calculateData(datas[8][6],asheCnt), calculateData(datas[8][7],asheCnt),
                calculateData(datas[8][8],asheCnt), calculateData(datas[8][9],asheCnt), calculateData(datas[8][10],asheCnt),
                calculateData(datas[8][11],asheCnt), "평균 충격샷건 처치", "평균 다이너마이트 처치", "평균 BOB 처치", "평균 단독처치", "");

        playerDetailList.add(asheDetail);

        log.debug("{} >>>>>>> process 진행중 | ======== 시메트라 데이터 계산 시작 ==========", JOB_NAME);
        PlayerDetail symmetraDetail = new PlayerDetail((long)min, season, 9, "symmetra", "시메트라",
                calculateData(datas[9][0], symmetraCnt), "", "", calculateData(datas[9][1], symmetraCnt),
                "", calculateData(datas[9][2],symmetraCnt), calculateData(datas[9][3],symmetraCnt), calculateData(datas[9][4],symmetraCnt),
                calculateData(datas[9][5],symmetraCnt), calculateData(datas[9][6],symmetraCnt), calculateData(datas[9][7],symmetraCnt),
                calculateData(datas[9][8],symmetraCnt), calculateData(datas[9][9],symmetraCnt), calculateData(datas[9][10],symmetraCnt),
                calculateData(datas[9][11],symmetraCnt), "평균 감시포탑 처치", "평균 순간이동한 아군", "평균 단독처치", "", "");

        playerDetailList.add(symmetraDetail);

        log.debug("{} >>>>>>> process 진행중 | ======== 솜브라 데이터 계산 시작 ==========", JOB_NAME);
        PlayerDetail sombraDetail = new PlayerDetail((long)min, season, 10, "sombra", "솜브라",
                calculateData(datas[10][0], sombraCnt), "", "", calculateData(datas[10][1], sombraCnt),
                "", calculateData(datas[10][2],sombraCnt), calculateData(datas[10][3],sombraCnt), calculateData(datas[10][4],sombraCnt),
                calculateData(datas[10][5],sombraCnt), calculateData(datas[10][6],sombraCnt), calculateData(datas[10][7],sombraCnt),
                calculateData(datas[10][8],sombraCnt), calculateData(datas[10][9],sombraCnt), calculateData(datas[10][10],sombraCnt),
                calculateData(datas[10][11],sombraCnt), "평균 해킹한 적", "평균 EMP맞춘 적", "치명타 명중률", "평균 단독처치", "");

        playerDetailList.add(sombraDetail);

        log.debug("{} >>>>>>> process 진행중 | ======== 바스티온 데이터 계산 시작 ==========", JOB_NAME);
        PlayerDetail bastionDetail = new PlayerDetail((long)min, season, 11, "bastion", "바스티온",
                calculateData(datas[11][0], bastionCnt), "", "", calculateData(datas[11][1], bastionCnt),
                "", calculateData(datas[11][2],bastionCnt), calculateData(datas[11][3],bastionCnt), calculateData(datas[11][4],bastionCnt),
                calculateData(datas[11][5],bastionCnt), calculateData(datas[11][6],bastionCnt), calculateData(datas[11][7],bastionCnt),
                calculateData(datas[11][8],bastionCnt), calculateData(datas[11][9],bastionCnt), calculateData(datas[11][10],bastionCnt),
                calculateData(datas[11][11],bastionCnt), "평균 경계모드 처치", "평균 수색모드 처치", "평균 전차모드 처치", "평균 단독처치", "");

        playerDetailList.add(bastionDetail);

        log.debug("{} >>>>>>> process 진행중 | ======== 메이 데이터 계산 시작 ==========", JOB_NAME);
        PlayerDetail meiDetail = new PlayerDetail((long)min, season, 12, "mei", "메이",
                calculateData(datas[12][0], meiCnt), "", "", calculateData(datas[12][1], meiCnt),
                "", calculateData(datas[12][2],meiCnt), calculateData(datas[12][3],meiCnt), calculateData(datas[12][4],meiCnt),
                calculateData(datas[12][5],meiCnt), calculateData(datas[12][6],meiCnt), calculateData(datas[12][7],meiCnt),
                calculateData(datas[12][8],meiCnt), calculateData(datas[12][9],meiCnt), calculateData(datas[12][10],meiCnt),
                calculateData(datas[12][11],meiCnt), "평균 눈보라 처치", "평균 얼린적", "평균 단독처치", "", "");

        playerDetailList.add(meiDetail);

        log.debug("{} >>>>>>> process 진행중 | ======== 맥크리 데이터 계산 시작 ==========", JOB_NAME);
        PlayerDetail mccreeDetail = new PlayerDetail((long)min, season, 13, "mccree", "맥크리",
                calculateData(datas[13][0], mccreeCnt), "", "", calculateData(datas[13][1], mccreeCnt),
                "", calculateData(datas[13][2],mccreeCnt), calculateData(datas[13][3],mccreeCnt), calculateData(datas[13][4],mccreeCnt),
                calculateData(datas[13][5],mccreeCnt), calculateData(datas[13][6],mccreeCnt), calculateData(datas[13][7],mccreeCnt),
                calculateData(datas[13][8],mccreeCnt), calculateData(datas[13][9],mccreeCnt), calculateData(datas[13][10],mccreeCnt),
                calculateData(datas[13][11],mccreeCnt), "평균 난사 처치", "평균 황야의 무법자 처치", "치명타 명중률", "평균 단독처치", "");

        playerDetailList.add(mccreeDetail);

        log.debug("{} >>>>>>> process 진행중 | ======== 둠피스트 데이터 계산 시작 ==========", JOB_NAME);
        PlayerDetail doomfistDetail = new PlayerDetail((long)min, season, 14, "doomfist", "둠피스트",
                calculateData(datas[14][0], doomfistCnt), "", "", calculateData(datas[14][1], doomfistCnt),
                "", calculateData(datas[14][2],doomfistCnt), calculateData(datas[14][3],doomfistCnt), calculateData(datas[14][4],doomfistCnt),
                calculateData(datas[14][5],doomfistCnt), calculateData(datas[14][6],doomfistCnt), calculateData(datas[14][7],doomfistCnt),
                calculateData(datas[14][8],doomfistCnt), calculateData(datas[14][9],doomfistCnt), calculateData(datas[14][10],doomfistCnt),
                calculateData(datas[14][11],doomfistCnt), "평균 기술로 준 피해", "평균 보호막 생성량", "평균 파멸의 일격 처치", "평균 단독처치", "");

        playerDetailList.add(doomfistDetail);

        log.debug("{} >>>>>>> process 진행중 | ======== 겐지 데이터 계산 시작 ==========", JOB_NAME);
        PlayerDetail genjiDetail = new PlayerDetail((long)min, season, 15, "genji", "겐지",
                calculateData(datas[15][0], genjiCnt), "", "", calculateData(datas[15][1], genjiCnt),
                "", calculateData(datas[15][2],genjiCnt), calculateData(datas[15][3],genjiCnt), calculateData(datas[15][4],genjiCnt),
                calculateData(datas[15][5],genjiCnt), calculateData(datas[15][6],genjiCnt), calculateData(datas[15][7],genjiCnt),
                calculateData(datas[15][8],genjiCnt), calculateData(datas[15][9],genjiCnt), calculateData(datas[15][10],genjiCnt),
                calculateData(datas[15][11],genjiCnt), "평균 용검 처치", "평균 튕겨낸 피해량", "평균 단독처치", "", "");

        playerDetailList.add(genjiDetail);

        log.debug("{} >>>>>>> process 진행중 | ======== 오리사 데이터 계산 시작 ==========", JOB_NAME);
        PlayerDetail orisaDetail = new PlayerDetail((long)min, season, 16, "orisa", "오리사",
                calculateData(datas[16][0], orisaCnt), "", "", calculateData(datas[16][1], orisaCnt),
                "", calculateData(datas[16][2],orisaCnt), calculateData(datas[16][3],orisaCnt), calculateData(datas[16][4],orisaCnt),
                calculateData(datas[16][5],orisaCnt), calculateData(datas[16][6],orisaCnt), calculateData(datas[16][7],orisaCnt),
                calculateData(datas[16][8],orisaCnt), calculateData(datas[16][9],orisaCnt), calculateData(datas[16][10],orisaCnt),
                calculateData(datas[16][11],orisaCnt), "평균 공격력 증폭", "", "", "", "");

        playerDetailList.add(orisaDetail);

        log.debug("{} >>>>>>> process 진행중 | ======== 시그마 데이터 계산 시작 ==========", JOB_NAME);
        PlayerDetail sigmaDetail = new PlayerDetail((long)min, season, 17, "sigma", "시그마",
                calculateData(datas[17][0], sigmaCnt), "", "", calculateData(datas[17][1], sigmaCnt),
                "", calculateData(datas[17][2],sigmaCnt), calculateData(datas[17][3],sigmaCnt), calculateData(datas[17][4],sigmaCnt),
                calculateData(datas[17][5],sigmaCnt), calculateData(datas[17][6],sigmaCnt), calculateData(datas[17][7],sigmaCnt),
                calculateData(datas[17][8],sigmaCnt), calculateData(datas[17][9],sigmaCnt), calculateData(datas[17][10],sigmaCnt),
                calculateData(datas[17][11],sigmaCnt), "목숨당 흡수한 피해", "평균 중력붕괴 처치", "평균 강착 처치", "", "");

        playerDetailList.add(sigmaDetail);

        log.debug("{} >>>>>>> process 진행중 | ======== 자리야 데이터 계산 시작 ==========", JOB_NAME);
        PlayerDetail zaryaDetail = new PlayerDetail((long)min, season, 18, "zarya", "자리야",
                calculateData(datas[18][0], zaryaCnt), "", "", calculateData(datas[18][1], zaryaCnt),
                "", calculateData(datas[18][2],zaryaCnt), calculateData(datas[18][3],zaryaCnt), calculateData(datas[18][4],zaryaCnt),
                calculateData(datas[18][5],zaryaCnt), calculateData(datas[18][6],zaryaCnt), calculateData(datas[18][7],zaryaCnt),
                calculateData(datas[18][8],zaryaCnt), calculateData(datas[18][9],zaryaCnt), calculateData(datas[18][10],zaryaCnt),
                calculateData(datas[18][11],zaryaCnt), "평균 에너지", "평균 고에너지 처치", "평균 주는방볍", "평균 중력자탄 처치", "");

        playerDetailList.add(zaryaDetail);

        log.debug("{} >>>>>>> process 진행중 | ======== 라인하르트 데이터 계산 시작 ==========", JOB_NAME);
        PlayerDetail reinhardtDetail = new PlayerDetail((long)min, season, 19, "reinhardt", "라인하르트",
                calculateData(datas[19][0], reinhardtCnt), "", "", calculateData(datas[19][1], reinhardtCnt),
                "", calculateData(datas[19][2],reinhardtCnt), calculateData(datas[19][3],reinhardtCnt), calculateData(datas[19][4],reinhardtCnt),
                calculateData(datas[19][5],reinhardtCnt), calculateData(datas[19][6],reinhardtCnt), calculateData(datas[19][7],reinhardtCnt),
                calculateData(datas[19][8],reinhardtCnt), calculateData(datas[19][9],reinhardtCnt), calculateData(datas[19][10],reinhardtCnt),
                calculateData(datas[19][11],reinhardtCnt), "평균 대지분쇄 처치", "평균 돌진 처치", "평균 화염강타 처치", "", "");

        playerDetailList.add(reinhardtDetail);

        log.debug("{} >>>>>>> process 진행중 | ======== 디바 데이터 계산 시작 ==========", JOB_NAME);
        PlayerDetail dvaDetail = new PlayerDetail((long)min, season, 20, "D.Va", "디바",
                calculateData(datas[20][0], dvaCnt), "", "", calculateData(datas[20][1], dvaCnt),
                "", calculateData(datas[20][2],dvaCnt), calculateData(datas[20][3],dvaCnt), calculateData(datas[20][4],dvaCnt),
                calculateData(datas[20][5],dvaCnt), calculateData(datas[20][6],dvaCnt), calculateData(datas[20][7],dvaCnt),
                calculateData(datas[20][8],dvaCnt), calculateData(datas[20][9],dvaCnt), calculateData(datas[20][10],dvaCnt),
                calculateData(datas[20][11],dvaCnt), "평균 자폭 처치", "평균 메카호출", "", "", "");

        playerDetailList.add(dvaDetail);

        log.debug("{} >>>>>>> process 진행중 | ======== 윈스턴 데이터 계산 시작 ==========", JOB_NAME);
        PlayerDetail winstonDetail = new PlayerDetail((long)min, season, 21, "winston", "윈스턴",
                calculateData(datas[21][0], winstonCnt), "", "", calculateData(datas[21][1], winstonCnt),
                "", calculateData(datas[21][2],winstonCnt), calculateData(datas[21][3],winstonCnt), calculateData(datas[21][4],winstonCnt),
                calculateData(datas[21][5],winstonCnt), calculateData(datas[21][6],winstonCnt), calculateData(datas[21][7],winstonCnt),
                calculateData(datas[21][8],winstonCnt), calculateData(datas[21][9],winstonCnt), calculateData(datas[21][10],winstonCnt),
                calculateData(datas[21][11],winstonCnt), "평균 점프팩 처치", "평균 원시의분노 처치", "평균 밀친 적", "", "");

        playerDetailList.add(winstonDetail);

        log.debug("{} >>>>>>> process 진행중 | ======== 로드호그 데이터 계산 시작 ==========", JOB_NAME);
        PlayerDetail roadhogDetail = new PlayerDetail((long)min, season, 22, "roadhog", "로드호그",
               calculateData(datas[22][0], roadhogCnt), "", "", calculateData(datas[22][1], roadhogCnt),
                "", calculateData(datas[22][2],roadhogCnt), calculateData(datas[22][3],roadhogCnt), calculateData(datas[22][4],roadhogCnt),
                calculateData(datas[22][5],roadhogCnt), calculateData(datas[22][6],roadhogCnt), calculateData(datas[22][7],roadhogCnt),
                calculateData(datas[22][8],roadhogCnt), calculateData(datas[22][9],roadhogCnt), calculateData(datas[22][10],roadhogCnt),
                calculateData(datas[22][11],roadhogCnt), "평균 돼재앙 처치", "갈고리 명중률", "평균 끈 적", "목숭당 자힐량", "평균 단독처치");

        playerDetailList.add(roadhogDetail);

        log.debug("{} >>>>>>> process 진행중 | ======== 레킹볼 데이터 계산 시작 ==========", JOB_NAME);
        PlayerDetail wreckingballDetail = new PlayerDetail((long)min, season, 23, "wreckingball", "레킹볼",
                calculateData(datas[23][0], wreckingballCnt), "", "", calculateData(datas[23][1], wreckingballCnt),
                "", calculateData(datas[23][2],wreckingballCnt), calculateData(datas[23][3],wreckingballCnt), calculateData(datas[23][4],wreckingballCnt),
                calculateData(datas[23][5],wreckingballCnt), calculateData(datas[23][6],wreckingballCnt), calculateData(datas[23][7],wreckingballCnt),
                calculateData(datas[23][8],wreckingballCnt), calculateData(datas[23][9],wreckingballCnt), calculateData(datas[23][10],wreckingballCnt),
                calculateData(datas[23][11],wreckingballCnt), "평균 갈고리 처치", "평균 파일드라이버 처치", "평균 지뢰밭 처치", "", "");

        playerDetailList.add(wreckingballDetail);

        log.debug("{} >>>>>>> process 진행중 | ======== 모이라 데이터 계산 시작 ==========", JOB_NAME);
        PlayerDetail moiraDetail = new PlayerDetail((long)min, season, 24, "moira", "모이라",
                calculateData(datas[24][0], moiraCnt), "", "", calculateData(datas[24][1], moiraCnt),
                "", calculateData(datas[24][2],moiraCnt), calculateData(datas[24][3],moiraCnt), calculateData(datas[24][4],moiraCnt),
                calculateData(datas[24][5],moiraCnt), calculateData(datas[24][6],moiraCnt), calculateData(datas[24][7],moiraCnt),
                calculateData(datas[24][8],moiraCnt), calculateData(datas[24][9],moiraCnt), calculateData(datas[24][10],moiraCnt),
                calculateData(datas[24][11],moiraCnt), "평균 융화 처치", "평균 융화 힐", "목숭당 자힐량", "", "");

        playerDetailList.add(moiraDetail);

        log.debug("{} >>>>>>> process 진행중 | ======== 아나 데이터 계산 시작 ==========", JOB_NAME);
        PlayerDetail anaDetail = new PlayerDetail((long)min, season, 25, "ana", "아나",
                calculateData(datas[25][0], anaCnt), "", "", calculateData(datas[25][1], anaCnt),
                "", calculateData(datas[25][2],anaCnt), calculateData(datas[25][3],anaCnt), calculateData(datas[25][4],anaCnt),
                calculateData(datas[25][5],anaCnt), calculateData(datas[25][6],anaCnt), calculateData(datas[25][7],anaCnt),
                calculateData(datas[25][8],anaCnt), calculateData(datas[25][9],anaCnt), calculateData(datas[25][10],anaCnt),
                calculateData(datas[25][11],anaCnt), "평균 나노강화제 주입", "평균 생체수류탄 처치", "평균 재운적", "", "");

        playerDetailList.add(anaDetail);

        log.debug("{} >>>>>>> process 진행중 | ======== 브리기테 데이터 계산 시작 ==========", JOB_NAME);
        PlayerDetail brigitteDetail = new PlayerDetail((long)min, season, 26, "brigitte", "브리기테",
                calculateData(datas[26][0], brigitteCnt), "", "", calculateData(datas[26][1], brigitteCnt),
                "", calculateData(datas[26][2],brigitteCnt), calculateData(datas[26][3],brigitteCnt), calculateData(datas[26][4],brigitteCnt),
                calculateData(datas[26][5],brigitteCnt), calculateData(datas[26][6],brigitteCnt), calculateData(datas[26][7],brigitteCnt),
                calculateData(datas[26][8],brigitteCnt), calculateData(datas[26][9],brigitteCnt), calculateData(datas[26][10],brigitteCnt),
                calculateData(datas[26][11],brigitteCnt), "목숨당 방어력 제공", "격려(패시브) 지속률", "", "", "");

        playerDetailList.add(brigitteDetail);

        log.debug("{} >>>>>>> process 진행중 | ======== 바티스트 데이터 계산 시작 ==========", JOB_NAME);
        PlayerDetail baptisteDetail = new PlayerDetail((long)min, season, 27, "baptiste", "바티스트",
                calculateData(datas[27][0], baptisteCnt), "", "", calculateData(datas[27][1], baptisteCnt),
                "", calculateData(datas[27][2],baptisteCnt), calculateData(datas[27][3],baptisteCnt), calculateData(datas[27][4],baptisteCnt),
                calculateData(datas[27][5],baptisteCnt), calculateData(datas[27][6],baptisteCnt), calculateData(datas[27][7],baptisteCnt),
                calculateData(datas[27][8],baptisteCnt), calculateData(datas[27][9],baptisteCnt), calculateData(datas[27][10],baptisteCnt),
                calculateData(datas[27][11],baptisteCnt), "평균 불사장치 세이브", "평균 강화메트릭스 사용", "", "", "");

        playerDetailList.add(baptisteDetail);

        log.debug("{} >>>>>>> process 진행중 | ======== 젠야타 데이터 계산 시작 ==========", JOB_NAME);
        PlayerDetail zenyattaDetail = new PlayerDetail((long)min, season, 28, "zenyatta", "젠야타",
                calculateData(datas[28][0], zenyattaCnt), "", "", calculateData(datas[28][1], zenyattaCnt),
                "", calculateData(datas[28][2],zenyattaCnt), calculateData(datas[28][3],zenyattaCnt), calculateData(datas[28][4],zenyattaCnt),
                calculateData(datas[28][5],zenyattaCnt), calculateData(datas[28][6],zenyattaCnt), calculateData(datas[28][7],zenyattaCnt),
                calculateData(datas[28][8],zenyattaCnt), calculateData(datas[28][9],zenyattaCnt), calculateData(datas[28][10],zenyattaCnt),
                calculateData(datas[28][11],zenyattaCnt), "평균 초월 힐", "", "", "", "");

        playerDetailList.add(zenyattaDetail);

        log.debug("{} >>>>>>> process 진행중 | ======== 루시우 데이터 계산 시작 ==========", JOB_NAME);
        PlayerDetail lucioDetail = new PlayerDetail((long)min, season, 29, "lucio", "루시우",
                calculateData(datas[29][0], lucioCnt), "", "", calculateData(datas[29][1], lucioCnt),
                "", calculateData(datas[29][2],lucioCnt), calculateData(datas[29][3],lucioCnt), calculateData(datas[29][4],lucioCnt),
                calculateData(datas[29][5],lucioCnt), calculateData(datas[29][6],lucioCnt), calculateData(datas[29][7],lucioCnt),
                calculateData(datas[29][8],lucioCnt), calculateData(datas[29][9],lucioCnt), calculateData(datas[29][10],lucioCnt),
                calculateData(datas[29][11],lucioCnt), "평균 소리방벽 사용", "", "", "", "");

        playerDetailList.add(lucioDetail);

        log.debug("{} >>>>>>> process 진행중 | ======== 메르시 데이터 계산 시작 ==========", JOB_NAME);
        PlayerDetail mercyDetail = new PlayerDetail((long)min, season, 30, "mercy", "메르시",
                calculateData(datas[30][0], mercyCnt), "", "", calculateData(datas[30][1], mercyCnt),
                "", calculateData(datas[30][2],mercyCnt), calculateData(datas[30][3],lucioCnt), calculateData(datas[30][4],mercyCnt),
                calculateData(datas[30][5],mercyCnt), calculateData(datas[30][6],mercyCnt), calculateData(datas[30][7],mercyCnt),
                calculateData(datas[30][8],mercyCnt), calculateData(datas[30][9],mercyCnt), calculateData(datas[30][10],mercyCnt),
                calculateData(datas[03][11],mercyCnt), "평균 부활", "평균 공격력 증폭", "", "", "");

        playerDetailList.add(mercyDetail);



        cdDto.setPlayerDetailList(playerDetailList);

        return cdDto;
    }

    public UpdateTierDetailDataBatchJobProcessor setMinMax(int min, int max) {
        this.min = min;
        this.max = max;
        return this;
    }

    public UpdateTierDetailDataBatchJobProcessor setSeason(long season) {
        this.season = season;
        return this;
    }

    private String calculateData(double data, int cnt) {
        Double doubleData = Math.round((data /(double)cnt) * 100) / 100.0;
        log.debug("{} >>>>>>> process 진행중 | 내부 데이터 계산 시작 : {} / {} = {}", JOB_NAME, data, cnt, doubleData);
        return doubleData.toString();
    }

}


