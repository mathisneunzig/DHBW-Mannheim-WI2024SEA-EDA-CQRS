<script setup>
import square from './assets/square.png'
</script>

<template>
	<h1>Tamagochi: CQRS + EDA</h1>
	<div>
		<img :src="square"/>
	</div>
	<div>
		<p>Hunger: xxx</p>
		<p>Sleep: xxx</p>
		<p>Boredom: xxx</p>
	</div>	
	<div style="display: flex; justify-content: center; gap: 30px;">
		<button>Feed</button>
		<button>Sleep</button>
		<button>Play</button>
	</div>
</template>

<style scoped>
</style>
