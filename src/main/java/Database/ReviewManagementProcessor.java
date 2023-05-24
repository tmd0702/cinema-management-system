package Database;

import Utils.Response;

public class ReviewManagementProcessor extends Processor{
    public ReviewManagementProcessor() {
        super();
        setDefaultDatabaseTable("REVIEW");
    }

    @Override
    public Response getData(int from, int quantity, String queryCondition, String sortQuery) {
        if (queryCondition.length() > 0) {
            queryCondition = "R.MOVIE_ID = M.ID AND R.USER_ID = U.ID AND U.USER_CATEGORY_ID = UC.ID AND " + queryCondition;
        } else {
            queryCondition = "R.MOVIE_ID = M.ID AND R.USER_ID = U.ID AND U.USER_CATEGORY_ID = UC.ID";
        }
        Response response = select("UC.CATEGORY, U.USERNAME, M.ID AS MOVIE_ID, R.RATING, R.COMMENT, R.DATE", from, quantity, queryCondition, sortQuery, "REVIEW R, USERS U, MOVIES M, USER_CATEGORY UC");
        return response;
    }
}
