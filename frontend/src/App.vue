<script setup>
import square from './assets/square.png'
import { ref } from 'vue'
import happy from './assets/tamagochi/happy.gif'
import eating from './assets/tamagochi/eating.gif'
import playing from './assets/tamagochi/eating.gif'
import sad from './assets/tamagochi/eating.gif'
import sleeping from './assets/tamagochi/eating.gif'
import shappy from './assets/tamagochi/eating.gif'
import ssad from './assets/tamagochi/eating.gif'
import vhappy from './assets/tamagochi/eating.gif'
import hungerbar from './assets/hungerbar.png'
import boredbar from './assets/boredbar.png'
import tiredbar from './assets/tiredbar.png'

const currentGif = ref(happy)
</script>

<template>
	<h1>SquirtleGotchi</h1>
	<div style="position: relative; display: inline-block;">
		<img :src="currentGif" class="tamagochi-gif"/>
	</div>
	<div>
		<p><img :src="hungerbar" class="status-icon"/> Hunger: xxx</p>
		<p><img :src="tiredbar" class="status-icon"/> Sleep: xxx</p>
		<p><img :src="boredbar" class="status-icon"/> Boredom: xxx</p>
	</div>	
	<div style="display: flex; justify-content: center; gap: 30px;">
		<button>Feed</button>
		<button>Sleep</button>
		<button>Play</button>
	</div>
</template>

<style scoped>
.tamagochi-gif {
	border: 10px solid #135d8b;
	border-radius: 8px;
	padding: 10px;
	background-color: #f0f0f0;
	display:block;
}
.status-icon {
	height: 1em;
	width: auto;
	vertical-align: middle;
}
</style>
