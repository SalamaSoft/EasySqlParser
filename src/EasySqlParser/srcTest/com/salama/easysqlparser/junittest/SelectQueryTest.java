package com.salama.easysqlparser.junittest;

import static org.junit.Assert.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;

import com.salama.easysqlparser.base.ISelectQueryHandler;
import com.salama.easysqlparser.base.SqlPart;
import com.salama.easysqlparser.base.SqlPartUtil;
import com.salama.easysqlparser.base.SqlPart.SqlPartType;
import com.salama.easysqlparser.base.element.TableName;
import com.salama.easysqlparser.query.SelectQuery;
import com.salama.easysqlparser.update.DeleteSql;
import com.salama.easysqlparser.update.InsertSql;
import com.salama.easysqlparser.update.UpdateSql;
import com.salama.easysqlparser.util.CharDelimStringTokenizer;
import com.salama.easysqlparser.util.SqlParseException;

public class SelectQueryTest {
	private final static Logger logger = Logger.getLogger(SelectQueryTest.class);
	
	@Ignore
	public void testSelect1() {
		String sql = "select distinct a.* , d.brandCd  , d.brandLogo  from (select activityCd             , pictureId             , websiteUrl             , promoTitle             , bankType             , promoBriefDesc             , beginDate             , endDate             , supportCard             , promoDesc             , promoDetail             , coveredCityDisp             , recommendFlg          from Activity         where activityCd = ?           and activeFlg = 1) a  inner join (select * from ActivityShop                where activityCd = '001'                 and (ifnull(endDate, '') = ''                  or (beginDate <= date_format(now(), '%Y%m%d')                 and date_format(now(), '%Y%m%d') <= endDate))             ) b on a.activityCd = b.activityCd  inner join Shop c on b.shopCd = c.shopCd                   and c.activeFlg = 1  left join Brand d on c.brandCd = d.brandCd   order by d.brandCd ";
		
		testSelectSql(sql);
	}

	@Ignore
	public void testSelect2() {
		String sql = " select a.* , b.shopCd , case when ifnull(b.endDate, '') = '' then a.endDate else b.endDate end as shopEndDate , case when ifnull(b.endDate, '') = '' then a.beginDate else b.beginDate end as shopBeginDate , case when ifnull(b.supportCard, '') = '' then a.supportCard else b.supportCard end as shopSupportCard , case when ifnull(b.promoTitle, '') = '' then a.promoTitle else b.supportCard end as shopPromoTitle , case when ifnull(b.promoBriefDesc, '') = '' then a.promoBriefDesc else b.promoBriefDesc end as shopPromoBriefDesc , case when ifnull(b.promoDetail, '') = '' then a.promoDetail else b.promoDetail end as shopPromoDetail , case when ifnull(b.promoDesc, '') = '' then a.promoDesc else b.promoDesc end as shopPromoDesc from Activity a inner join ActivityShop b on a.activityCd = b.activityCd where a.activeFlg = 1";
		
		testSelectSql(sql);
	}
	
	@Ignore
	public void testSelect3() {
		String sql = "select * from t1 where t1.f1 = 'a' and (t1.f2 = 'b' or t1.f3='3') union all (select * from t2 where t2.id = '001' or (t2.f1='002' or t2.f2='003')) ";
		testSelectSql(sql);
	}
	
	public void testSelect4() {
		String sql = "select A.threadId , A.threadTitle , A.threadContent , case when IFNULL(A.answerCount,0) > 0 then D.memberLogo        else B.memberLogo   end memberLogo , case when IFNULL(A.answerCount,0) > 0 then D.agreeCount        else B.agreeCount   end count , C.answerId , case when IFNULL(A.answerCount,0) > 0 then C.answerContent        else A.threadContent   end answerContent  from DiscussThread A  left join DeveloperGroupUser B         on A.createUserId = B.userId  left join (select TB.* from (select threadId,max(answerId) answerId from DsicussAnswer group by threadId) TA  left join DsicussAnswer TB on TA.threadId = TB.threadId and TA.answerId = TB.answerId) C         on A.threadId = C.threadId  left join DeveloperGroupUser D         on C.createUserId = D.userId order by A.createTime desc";
		testSelectSql(sql);
	}
	
	@Test
	public void testSelect5() {
		String sql = "SELECT A.*,B.nickName,B.memberLogo FROM (SELECT T1.threadId,T1.answerId,'' AS commentId,T1.answerContent AS content,T1.createTime,T1.createUserId,'1' AS msgType FROM DiscussAnswer T1 INNER JOIN DiscussThread T2 ON T2.threadId = T1.threadId WHERE T2.createUserId = '010004ae2b8d5901'  UNION ALL SELECT T3.threadId,T3.answerId,'' AS commentId,T3.answerContent AS content,T3.createTime,T3.createUserId,'2' AS msgType FROM DiscussAnswer T3 INNER JOIN ThreadAttention T4 ON T4.attentionThreadId = T3.threadId WHERE T4.attentionUserId = '010004ae2b8d5901' UNION ALL SELECT T5.threadId,T5.answerId,T5.commentId,T5.commentContent AS content,T5.createTime,T5.createUserId,'3' AS msgType FROM AnswerComment T5 INNER JOIN DiscussAnswer T6 ON T6.answerId = T5.answerId WHERE T6.createUserId = '010004ae2b8d5901') A INNER JOIN DeveloperGroupUser B ON B.userId = A.createUserId ORDER BY A.createTime DESC LIMIT 0,5";
		testSelectSql(sql);
	}
	
	private final static char[] TokenDelims = new char[] {' ', '(', ')'};
	private final static boolean[] IsDelimAsToken = new boolean[] {false, true, true};
	
	@Ignore
	public void testCharDelimsTokenizer() {
		String sql = "select * from t1 where t1.f1 = 'a' and (t1.f2 = 'b' or t1.f3='3') union all (select * from t2 where t2.id = '001' or (t2.f1='002' or t2.f2='003')) ";

		String token;
		CharDelimStringTokenizer stk = new CharDelimStringTokenizer(new StringBuilder(sql),
				TokenDelims, IsDelimAsToken);
		while(stk.hasMoreTokens()) {
			token = stk.nextToken();
			
			logger.debug("token:" + token);
		}
	}
	
	@Ignore
	public void testCharDelimsTokenizer2() {
		String sql = " and (t1.f2 = 'b' or t1.f3='3') union all (select * from t2 where t2.id = '001' or (t2.f1='002' or t2.f2='003')) ";

		String token;
		CharDelimStringTokenizer stk = new CharDelimStringTokenizer(new StringBuilder(sql), TokenDelims, IsDelimAsToken);
		while(stk.hasMoreTokens()) {
			token = stk.nextToken();
			
			logger.debug("token:" + token);
		}
	}

	@Ignore
	public void testUpdate1() {
		String sql = "update AppPointShopGoods set downloadFlg = 1 , updateTime = ? where consumeType = ?  and consumeRowNum = ? and shopGoodsSeq = ?  and downloadFlg = 0";
		
		testUpdateSql(sql);
	}
	
	@Ignore
	public void testInsert1() {
		String sql = "insert into Test1 (item1, item2, item3) values ('aaa', 'bbb', 'ccc')";
		
		String testS = null;
		try {
			testS = URLEncoder.encode("+ +", "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		testInsertSql(sql);
	}
	
	private void testSelectSql(String sql) {
		ISelectQueryHandler queryHandler = new ISelectQueryHandler() {
			@Override
			public void handleQuery(SelectQuery query) {
				logger.debug("EditingSql: " + query.getEditingSql().toString());
				
				StringBuilder sql = new StringBuilder();
				try {
					query.toSql(sql);
					logger.debug("toSql:" + sql);
					
					List<TableName> tables = SqlPartUtil.getFromTables(
							query.find1stSqlPartByType(SqlPartType.From));
					StringBuilder sqlTables = new StringBuilder();
					for(int i = 0; i < tables.size(); i++) {
						sqlTables.append(tables.get(i).tableName).append(" ");
					}
					logger.debug("fromTables:" + sqlTables.toString());
				} catch (SqlParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		
		SelectQuery query = null;
		try {
			query = new SelectQuery(sql, queryHandler);
			
			StringBuilder sqlAll = new StringBuilder();
			query.toSql(sqlAll);
			
			logger.debug(sqlAll);
		} catch (SqlParseException e) {
			logger.error("", e);
		}
	}
	
	public void testUpdateSql(String sql) {
		try {
			UpdateSql sqlObj = new UpdateSql(sql);
			
			StringBuilder sqlOutput = new StringBuilder();
			sqlObj.toSql(sqlOutput);
			
			logger.debug("sql output:" + sqlOutput.toString());
		} catch (SqlParseException e) {
			logger.error("", e);
		}
	}
	
	public void testInsertSql(String sql) {
		try {
			InsertSql sqlObj = new InsertSql(sql);

			StringBuilder sqlOutput = new StringBuilder();
			sqlObj.toSql(sqlOutput);
			
			logger.debug("sql output:" + sqlOutput.toString());
		} catch (SqlParseException e) {
			logger.error("", e);
		}
	}

	public void testDeleteSql(String sql) {
		try {
			DeleteSql sqlObj = new DeleteSql(sql);

			StringBuilder sqlOutput = new StringBuilder();
			sqlObj.toSql(sqlOutput);
			
			logger.debug("sql output:" + sqlOutput.toString());
		} catch (SqlParseException e) {
			logger.error("", e);
		}
	}
	
}


