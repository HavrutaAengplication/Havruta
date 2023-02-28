<template>
  <div class="popup-container">
    <div class="popup">
      <h2>Add Group</h2>
      <label for="group-name-input">Group Name:</label>
      <input id="group-name-input" v-model="groupName" type="text">
      <div>
        <button @click="closePopup">Cancel</button>
        <button @click="sendGroupName">Send</button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      groupName: '',
    };
  },
  methods: {
    closePopup() {
      this.$emit('close-popup');
    },
    sendGroupName() {
      this.$emit('send-group-name', this.groupName);
    },
  },
  mounted() {
    const popup = this.$el.querySelector('.popup');
    popup.style.marginTop = `-${popup.offsetHeight / 2}px`;
    popup.style.marginLeft = `-${popup.offsetWidth / 2}px`;
  },
};
</script>

<style>
.popup-container {
  position: fixed;
  top: 0;
  left: 0;
  height: 100%;
  width: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.popup {
  position: absolute;
  top: 50%;
  left: 50%;
  background-color: #ffffff;
  border-radius: 8px;
  padding: 16px;
  box-shadow: 0 0 8px rgba(0, 0, 0, 0.3);
  text-align: center;
}

.popup label {
  display: block;
  margin-bottom: 8px;
}

.popup input {
  padding: 8px;
  border-radius: 4px;
  border: none;
  margin-bottom: 16px;
}

.popup button {
  background-color: #2196F3;
  color: #ffffff;
  border: none;
  border-radius: 4px;
  padding: 8px 16px;
  margin-right: 8px;
}

.popup button:last-child {
  margin-right: 0;
}
</style>