<template>
  <div>
    <button @click="requestAccessToken">
      <img src="https://developers.google.com/identity/images/g-logo.png" alt="Google Login Button">
      Request Access Token
    </button>
    <button @click="requestSignUp">
      Sign Up with Google
    </button>
  </div>
</template>

<script>
export default {
  data() {
    return {
      googleAuth: null,
      googleUser: null,
      clientId: '430753499228-gon5t4qggupn872loh7cekulf8hretuq.apps.googleusercontent.com',
      scope: 'email',
    };
  },
  mounted() {
    gapi.load('auth2', () => {
      gapi.auth2.init({
        client_id: this.clientId,
        scope: this.scope,
      }).then((auth) => {
        this.googleAuth = auth;
        this.googleUser = this.googleAuth.currentUser.get();
      });
    });
  },
  methods: {
    requestAccessToken() {
      this.googleAuth.grantOfflineAccess().then((response) => {
        console.log(response.code);
      });
    },
    requestSignUp() {
      gapi.client.load('oauth2', 'v2', () => {
        gapi.client.oauth2.userinfo.get().execute((response) => {
          console.log(response);
        });
      });
    },
  },
};
</script>