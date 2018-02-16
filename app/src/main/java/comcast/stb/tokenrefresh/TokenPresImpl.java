package comcast.stb.tokenrefresh;


import comcast.stb.entity.NewToken;

/**
 * Created by anilpaudel on 1/9/18.
 */

public class TokenPresImpl implements TokenRefreshApiInterface.TokenRefreshPresenter, TokenRefreshApiInterface.TokenRefreshListener {
    TokenRefreshApiInterface.TokenRefreshView tokenRefreshView;
    TokenRefreshApiInterface.TokenRefreshInteractor tokenRefreshInteractor;

    public TokenPresImpl(TokenRefreshApiInterface.TokenRefreshView tokenRefreshView) {
        this.tokenRefreshView = tokenRefreshView;
        tokenRefreshInteractor = new TokenRefreshModel(this);
    }

    @Override
    public void refreshTheToken(String oldToken) {
        tokenRefreshInteractor.generateNewToken(oldToken);
    }

    @Override
    public void tokenIsRefreshed(NewToken newToken) {
        tokenRefreshView.newToken(newToken);
    }

    @Override
    public void onError(String message) {
        tokenRefreshView.onError(message);
    }

    @Override
    public void logout() {
        tokenRefreshView.logout();
    }
}
