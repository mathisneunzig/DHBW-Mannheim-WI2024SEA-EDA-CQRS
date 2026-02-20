<script setup>
import square from './assets/square.png'
import { ref } from 'vue'
import happy from './assets/tamagochi/happy.gif'
import eating from './assets/tamagochi/eating.gif'
import playing from './assets/tamagochi/playing.gif'
import sad from './assets/tamagochi/sad.gif'
import sleeping from './assets/tamagochi/sleeping.gif'
import shappy from './assets/tamagochi/slighthappy.gif'
import ssad from './assets/tamagochi/slightsad.gif'
import vhappy from './assets/tamagochi/veryhappy.gif'
import hungerbar from './assets/hungerbar.png'
import boredbar from './assets/boredbar.png'
import tiredbar from './assets/tiredbar.png'
import apple from './assets/icons/apple.png'
import bed from './assets/icons/bed.png'
import pokeball from './assets/icons/pokeball.png'
import border from './assets/border.png'

// Button-Bilder importieren
import feedButton from './assets/buttons/feedbutton.png'
import sleepButton from './assets/buttons/sleepbutton.png'
import playButton from './assets/buttons/playbutton.png'

const currentGif = ref(happy)
const disabled = ref(false)

function changeGif(newGif) {
	if(disabled.value) return

	disabled.value = true
	currentGif.value = newGif

	setTimeout(() => {
		disabled.value = false
		currentGif.value = happy
	}, 5000)
}
</script>

<template>
	<h1>SquirtleGotchi</h1>
	<div class="gif-container">
		<img :src="currentGif" class="tamagochi-gif"/>
		<img :src="border" class="border-frame"/>
		
	</div>
	<div>
		<p><img :src="hungerbar" class="status-icon"/> Hunger: xxx</p>
		<p><img :src="tiredbar" class="status-icon"/> Sleep: xxx</p>
		<p><img :src="boredbar" class="status-icon"/> Boredom: xxx</p>
	</div>	
	<div style="display: flex; justify-content: center; gap: 30px;">
		<img 
			:src="feedButton" 
			@click="!disabled && changeGif(eating)"
			:class="['button-image', { 'disabled': disabled }]"
			alt="Feed"
		/>
		<img 
			:src="sleepButton" 
			@click="!disabled && changeGif(sleeping)"
			:class="['button-image', { 'disabled': disabled }]"
			alt="Sleep"
		/>
		<img 
			:src="playButton" 
			@click="!disabled && changeGif(playing)"
			:class="['button-image', { 'disabled': disabled }]"
			alt="Play"
		/>
	</div>
</template>

<style scoped>
.gif-container {
	position: relative;
	display: inline-block;
	width: 500px;
	height: 350px;
	padding: 30px;
}

.tamagochi-gif {
width: 100%;
height: 100%;
object-fit: cover;
display: block;
}

.border-frame {
  position: absolute;
  top: -190px;
  left: -545px;
  width: calc(100% + 1150px);
  height: calc(100% + 550px);
  margin: -30px;
  pointer-events: none;
  z-index: 10;
}

.status-icon {
	height: 1em;
	width: auto;
	vertical-align: middle;
}
button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}
.button-image {
	cursor: pointer;
	height: auto;
	width: 200px;
	filter: drop-shadow(0 4px 8px rgba(0, 0, 0, 0.3));
}

.button-image:hover:not(.disabled) {
	transform: translateY(-5px) scale(1.05);
	filter: drop-shadow(0 8px 16px rgba(0, 0, 0, 0.4));
}

.button-image:active:not(.disabled) {
	transform: translateY(-2px) scale(1.02);
}

.button-image.disabled {
	opacity: 0.5;
	cursor: not-allowed;
	filter: grayscale(70%) drop-shadow(0 4px 8px rgba(0, 0, 0, 0.2));
}
</style>
