const CLIENT_ID = "16ce3ad458b7e6c386dbc6cef4b73b45";
const REDIRECT_URI =  "http://localhost:8081/loginSuccess";

export const KAKAO_AUTH_URL = `https://kauth.kakao.com/oauth/authorize?client_id=${CLIENT_ID}&redirect_uri=${REDIRECT_URI}&response_type=code`;