<template>
  <div id="app">
    <router-view></router-view>  
   
  </div>
</template>

<script>


export default {
  name: 'App',
 
};
</script>

<style>
nav {
  margin-bottom: 20px;
}
ul {
  list-style-type: none;
  padding: 0;
}
li {
  display: inline-block;
  margin-right: 10px;
}
</style>