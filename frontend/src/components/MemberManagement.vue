<template>
  <div>
    <h1>Member Management Page</h1>
    <table>
      <thead>
      <tr>
        <th>ID</th>
        <th>Name</th>
        <th>Is Admin</th>
        <th>Action</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="(member, index) in members" :key="index">
        <td>{{ member.id }}</td>
        <td>{{ member.name }}</td>
        <td>
          <v-checkbox v-model="member.isAdmin" :label="`Admin`" />
        </td>
        <td>
          <v-btn @click="dismissMember(member.id)" color="red"
          >Dismiss</v-btn
          >
        </td>
      </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
import axios from "axios";
export default {
  data() {
    return {
      members: [
        { id: 1, name: "John", isAdmin: true },
        { id: 2, name: "Alice", isAdmin: false },
        { id: 3, name: "Bob", isAdmin: true },
        { id: 4, name: "Cathy", isAdmin: false },
        { id: 5, name: "David", isAdmin: false },
        { id: 6, name: "Eva", isAdmin: true },
        { id: 7, name: "Frank", isAdmin: false },
        { id: 8, name: "Grace", isAdmin: true },
        { id: 9, name: "Harry", isAdmin: false },
        { id: 10, name: "Iris", isAdmin: true },
      ],
    };
  },
  computed: {
    token() {
      return this.$route.query.token
    },
    headers() {
      return {Authorization: this.token}
    },
  },
  methods: {
    dismissMember(id) {
      axios
          .delete(`https://your-api-url/members/${id}`,{
            headers: this.headers
          })
          .then((response) => {
            // remove the member from the list
            console.log(response);
            this.members = this.members.filter((member) => member.id !== id);
          })
          .catch(error => {
            alert(error);
          });
    },
  },
};
</script>
