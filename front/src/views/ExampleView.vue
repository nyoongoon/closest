<template>
  <div id="app">
    <svg id="svg">
      <line
          v-for="(node, index) in nodes"
          :key="index"
          :x1="centerNode.x + centerNodeSize / 2"
          :y1="centerNode.y + centerNodeSize / 2"
          :x2="node.position.x + nodeSize / 2"
          :y2="node.position.y + nodeSize / 2"
          stroke="black"
      />
    </svg>
    <div
        class="node center-node"
        :style="{ top: centerNode.y + 'px', left: centerNode.x + 'px', width: centerNodeSize + 'px', height: centerNodeSize + 'px' }"
    ></div>
    <div
        v-for="(node, index) in nodes"
        :key="index"
        class="node"
        :style="{ ...node.style, width: nodeSize + 'px', height: nodeSize + 'px' }"
        @mouseover="handleMouseOver(index)"
        @mouseleave="handleMouseLeave(index)"
    ></div>
    <div v-if="showLoginModal" class="login-modal">
      <div class="modal-content">
        <h2>Login</h2>
        <form>
          <label for="username">Username:</label>
          <input type="text" id="username" name="username" />
          <label for="password">Password:</label>
          <input type="password" id="password" name="password" />
          <div class="button-group">
            <button type="button" class="signup-button">Sign Up</button>
            <button type="submit" class="login-button">Login</button>
          </div>
        </form>
      </div>
    </div>
    <div class="toggle-button" @click="toggleTab"></div>
    <div v-if="showSideTab" class="side-tab"></div>
  </div>
</template>







<script lang="ts">
import { defineComponent, reactive, onMounted, ref } from 'vue';

interface Node {
  position: { x: number; y: number };
  style: Record<string, string>;
  isStopped: boolean;
}

export default defineComponent({
  name: 'App',
  setup() {
    const centerNode = reactive({ x: window.innerWidth / 2, y: window.innerHeight / 2 });
    const centerNodeSize = 60;
    const nodeSize = 40;
    const nodeRangeFactor = 2; // 노드가 떠도는 범위를 조절하기 위한 상수

    const getRandomPosition = () => {
      return {
        x: centerNode.x + (Math.random() * window.innerWidth / 4 - window.innerWidth / 8) * nodeRangeFactor,
        y: centerNode.y + (Math.random() * window.innerHeight / 4 - window.innerHeight / 8) * nodeRangeFactor,
      };
    };

    const nodes = reactive<Node[]>([
      { position: getRandomPosition(), style: {}, isStopped: false },
      { position: getRandomPosition(), style: {}, isStopped: false },
      { position: getRandomPosition(), style: {}, isStopped: false },
      { position: getRandomPosition(), style: {}, isStopped: false },
    ]);

    let intervalId: number | null = null;

    const startMovement = () => {
      intervalId = setInterval(() => {
        nodes.forEach((node) => {
          if (!node.isStopped) {
            const randomX = Math.random() * 0.5 - 0.25;
            const randomY = Math.random() * 0.5 - 0.25;
            node.position.x += randomX;
            node.position.y += randomY;
            node.style.top = `${node.position.y}px`;
            node.style.left = `${node.position.x}px`;
          }
        });
      }, 100);
    };

    const handleMouseOver = (index: number) => {
      nodes.forEach((node, i) => {
        if (i !== index) {
          node.isStopped = true;
        }
      });
      nodes[index].style.transform = 'scale(1.5)';
      nodes[index].style.zIndex = '1';
    };

    const handleMouseLeave = (index: number) => {
      nodes.forEach((node) => {
        node.isStopped = false;
      });
      nodes[index].style.transform = 'scale(1)';
      nodes[index].style.zIndex = '0';
    };

    const showLoginModal = ref(false);
    const showSideTab = ref(false);
    const sideTabThreshold = 10; // Distance from the right edge to show the side tab

    const handleMouseMove = (event: MouseEvent) => {
      if (!showLoginModal.value && !showSideTab.value && window.innerWidth - event.clientX <= sideTabThreshold) {
        moveScreenLeft();
      } else {
        resetScreenPosition();
      }
    };

    const moveScreenLeft = () => {
      document.getElementById('app')!.style.transform = 'translateX(-100px)';
    };

    const resetScreenPosition = () => {
      document.getElementById('app')!.style.transform = 'translateX(0)';
    };

    const toggleTab = () => {
      showSideTab.value = !showSideTab.value;
      if (showSideTab.value) {
        moveScreenLeft();
      } else {
        resetScreenPosition();
      }
    };

    onMounted(() => {
      startMovement();
      window.addEventListener('mousemove', handleMouseMove);
    });

    return {
      centerNode,
      centerNodeSize,
      nodeSize,
      nodes,
      handleMouseOver,
      handleMouseLeave,
      showLoginModal,
      showSideTab,
      toggleTab,
    };
  },
});
</script>









<style>
#app {
  position: relative;
  width: 100vw;
  height: 100vh;
  background-color: white;
  overflow: hidden;
  transition: transform 0.3s ease;
}

#svg {
  position: absolute;
  width: 100%;
  height: 100%;
}

.node {
  position: absolute;
  background-color: black;
  border-radius: 50%;
  transition: transform 0.3s, z-index 0.3s, top 0.05s, left 0.05s;
}

.center-node {
  background-color: black;
}

.login-modal {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 300px;
  background-color: white;
  border: 1px solid #ccc;
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  z-index: 1000;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 20px;
}

.modal-content {
  width: 100%;
}

.modal-content h2 {
  margin-bottom: 20px;
  text-align: center;
}

.modal-content form {
  display: flex;
  flex-direction: column;
}

.modal-content label {
  margin-bottom: 5px;
}

.modal-content input {
  margin-bottom: 15px;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
}

.button-group {
  display: flex;
  justify-content: space-between;
}

.signup-button,
.login-button {
  width: 48%;
  padding: 10px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.signup-button:hover,
.login-button:hover {
  background-color: #0056b3;
}

.toggle-button {
  position: fixed; /* 이동하지 않는 고정 위치로 설정 */
  top: 10px;
  right: 10px;
  width: 30px;
  height: 30px;
  background-color: #007bff;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  z-index: 1000;
}

.side-tab {
  position: fixed;
  top: 50px;
  right: 0;
  width: 7.5px; /* Reduced width to 75% of original */
  height: calc(100% - 50px); /* Reduced height to start below the toggle button */
  background-color: rgba(0, 0, 0, 0.5);
  border-left: 1px solid rgba(204, 204, 204, 0.5); /* Thin gray solid border with transparency */
  cursor: pointer;
  z-index: 1000;
}
</style>











