package com.salama.easysqlparser.junittest;

public class SqlParserTest {
	/*	
	PatternCompiler _compiler = new Perl5Compiler();
	
	public static void main(String[] args) {
		SqlParserTest test = new SqlParserTest();
		test.testSelectQueryWorkLoading();
	}
	
	
	@Ignore
	public void testSelectQueryWorkLoading() {
		for(int i = 0; i < 1000; i++) {
			Thread t = new Thread(new Runnable() {
				
				@Override
				public void run() {
					testSelectQuery();
				}
			});
			t.start();
		}
	}
	
	@Test
	public void testSelectQuery() {
		//String sql = "select * From Developer t1 \njoin DeveloperApp t2 on t2.developerId = t1.developerId where appId = \n'te''''s''t01'";
		String sql = "select distinct a.* , d.brandCd  , d.brandLogo  from (select activityCd             , pictureId             , websiteUrl             , promoTitle             , bankType             , promoBriefDesc             , beginDate             , endDate             , supportCard             , promoDesc             , promoDetail             , coveredCityDisp             , recommendFlg          from Activity         where activityCd = ?           and activeFlg = 1) a  inner join (select * from ActivityShop                where activityCd = '001'                 and (ifnull(endDate, '') = ''                  or (beginDate <= date_format(now(), '%Y%m%d')                 and date_format(now(), '%Y%m%d') <= endDate))             ) b on a.activityCd = b.activityCd  inner join Shop c on b.shopCd = c.shopCd                   and c.activeFlg = 1  left join Brand d on c.brandCd = d.brandCd   order by d.brandCd ";
		ISelectQueryHandler queryHandler = new ISelectQueryHandler() {
			@Override
			public void handleQuery(SelectQuery query) {
				log("EditingSql: " + query.getEditingSql());
			}
		};
		
		IQueryJoinPartHandler queryJoinPartHandler = new IQueryJoinPartHandler() {
		};
		
		SelectQuery selectQuery = null;
		try {
			selectQuery = new SelectQuery(
					false,
					sql, 
					queryHandler, 
					queryJoinPartHandler);
		} catch (SqlParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Ignore
	public void testRegex1() {
		String regexStrTest1 = EasySqlParserProperties.singleton().getSettingValue("RegexStrLiteral");
		
		String input = "select * from Developer t1 join DeveloperApp t2 on t2.developerId = t1.developerId where appId = 'te''''s''t01'";
		
		try {
			testRegex(regexStrTest1, input);
		} catch (MalformedPatternException e) {
			e.printStackTrace();
		}
	}
	
	private void testRegex(String regexStr, String input) throws MalformedPatternException {
		log("testRegex() begin ----------------");
		
		long beginTime = System.currentTimeMillis();
		
		Pattern regexTest1 = _compiler.compile(regexStr, Perl5Compiler.CASE_INSENSITIVE_MASK);
		
		MatchResult matchResult = null;
		PatternMatcherInput matcherInput = new PatternMatcherInput(input);
		PatternMatcher matcher = new Perl5Matcher();
		int iStart = 0;
		int iEnd = 0;
		
		String token = null;

		while (matcher.contains(matcherInput, regexTest1)) {
			matchResult = matcher.getMatch();

			iStart = matchResult.beginOffset(0);
			iEnd = matchResult.endOffset(0);

			token = input.substring(iStart, iEnd);
			log("iStart:" + iStart + " iEnd:" + iEnd + " token:" + token);
		}
		
		log("testRegex() end. Takes MS:" + Long.toString(System.currentTimeMillis() - beginTime));
	}
	
	protected static void log(String msg) {
		System.out.println(msg);
	}
*/

}
