new Vue({
  el: '#app',
  data: function() {
	return { visible: false, activeIndex: "1" }
  },
methods: {
  handleSelect(key, keyPath) {
	console.log('不要傻了，这是不可能清除浏览器缓存的');
	if (key == 2) {
	  this.$message({
		  message: '清除缓存成功',
		  type: 'success',
		  center: true
	  });
	}
  }
}
})
