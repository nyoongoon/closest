<template>
  <div id="app">
    <svg id="svg">
      <!-- 중앙 노드를 원 형태로 표시 -->
      <circle :cx="centerNode.x" :cy="centerNode.y" :r="centerNodeSize / 2" fill="black" />

      <!-- 다른 노드와 중앙 노드를 선으로 연결 -->
      <line
          v-for="(node, index) in visibleNodes"
          :key="'line' + index"
          :x1="centerNode.x"
          :y1="centerNode.y"
          :x2="node.position.x + nodeSize / 2"
          :y2="node.position.y + nodeSize / 2"
          stroke="black"
          stroke-width="0.5"
          stroke-opacity="0.2"
      />
    </svg>
    <div
        v-for="(node, index) in visibleNodes"
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
          <input type="text" id="username" name="username"/>
          <label for="password">Password:</label>
          <input type="password" id="password" name="password"/>
          <div class="button-group">
            <button type="button" class="signup-button">Sign Up</button>
            <button type="submit" class="login-button">Login</button>
          </div>
        </form>
      </div>
    </div>
    <div class="toggle-button" @click="toggleTab"></div>
    <div v-if="showSideTab" class="side-tab" @mouseover="handleMouseOverSideTab"
         @mouseleave="handleMouseLeaveSideTab"></div>
  </div>
</template>

<script lang="ts">
import { defineComponent, onMounted, reactive, ref } from 'vue';

interface Node {
  position: { x: number; y: number };
  velocity: { x: number; y: number };
  style: Record<string, string>;
  isStopped: boolean;
}

export default defineComponent({
  name: 'App',
  setup() {
    const centerNode = reactive({ x: window.innerWidth / 2, y: window.innerHeight / 2 });
    const centerNodeSize = 60;
    const nodeSize = 40;
    const minDistance = 200; // 중앙에서 최소 거리
    const maxDistance = 300; // 중앙에서 최대 거리
    const visibleNodeCount = ref(4); // 화면에 보이는 노드의 개수

    const getRandomPosition = () => {
      const angle = Math.random() * Math.PI * 2;
      const r = minDistance + Math.random() * (maxDistance - minDistance);
      return {
        x: centerNode.x + r * Math.cos(angle),
        y: centerNode.y + r * Math.sin(angle),
      };
    };

    const getRandomVelocity = () => {
      return {
        x: (Math.random() * 2 - 1) * 2, // 속도를 30% 빠르게
        y: (Math.random() * 2 - 1) * 2, // 속도를 30% 빠르게
      };
    };

    const nodes = reactive<Node[]>([
      { position: getRandomPosition(), velocity: getRandomVelocity(), style: {}, isStopped: false },
      { position: getRandomPosition(), velocity: getRandomVelocity(), style: {}, isStopped: false },
      { position: getRandomPosition(), velocity: getRandomVelocity(), style: {}, isStopped: false },
      { position: getRandomPosition(), velocity: getRandomVelocity(), style: {}, isStopped: false },
    ]);

    const visibleNodes = ref<Node[]>([]);

    let intervalId: number | null = null;

    const startMovement = () => {
      intervalId = setInterval(() => {
        nodes.forEach((node) => {
          if (!node.isStopped) {
            node.position.x += node.velocity.x;
            node.position.y += node.velocity.y;

            // Change velocity slightly to create a more natural movement
            node.velocity.x += (Math.random() * 2 - 1) * 0.01;
            node.velocity.y += (Math.random() * 2 - 1) * 0.01;

            // Limit velocity to keep movement smooth
            node.velocity.x = Math.max(Math.min(node.velocity.x, 1.3), -1.3); // 최대 속도 1.3으로 제한
            node.velocity.y = Math.max(Math.min(node.velocity.y, 1.3), -1.3); // 최대 속도 1.3으로 제한

            // Check if the node is outside the circular boundary
            const dx = node.position.x - centerNode.x;
            const dy = node.position.y - centerNode.y;
            const distance = Math.sqrt(dx * dx + dy * dy);

            // Adjust position if outside the allowed distance range
            if (distance < minDistance || distance > maxDistance) {
              const angle = Math.atan2(dy, dx);
              const targetDistance = Math.min(maxDistance, Math.max(minDistance, distance));
              node.position.x = centerNode.x + targetDistance * Math.cos(angle);
              node.position.y = centerNode.y + targetDistance * Math.sin(angle);
              node.velocity.x *= -1;
              node.velocity.y *= -1;
            }

            node.style.top = `${node.position.y}px`;
            node.style.left = `${node.position.x}px`;
          }
        });

        // Update visible nodes
        visibleNodes.value = nodes.slice(0, visibleNodeCount.value);
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

    const isMouseOverSideTab = ref(false);

    const handleMouseOverSideTab = () => {
      isMouseOverSideTab.value = true;
    };

    const handleMouseLeaveSideTab = () => {
      isMouseOverSideTab.value = false;
      if (!showSideTab.value) {
        resetScreenPosition();
      }
    };

    const handleMouseMove = (event: MouseEvent) => {
      if (
          !showLoginModal.value &&
          !showSideTab.value &&
          window.innerWidth - event.clientX <= sideTabThreshold
      ) {
        moveScreenLeft();
      } else if (!isMouseOverSideTab.value) {
        resetScreenPosition();
      }
    };

    const moveScreenLeft = () => {
      document.getElementById('app')!.style.transform = 'translateX(-100px)';
    };

    const resetScreenPosition = () => {
      if (!isMouseOverSideTab.value) {
        document.getElementById('app')!.style.transform = 'translateX(0)';
      }
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
      handleMouseOverSideTab,
      handleMouseLeaveSideTab,
      visibleNodes,
      visibleNodeCount,
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
  transition: transform 0.3s, z-index 0.3s, top 0.1s, left 0.1s, width 0.3s, height 0.3s; /* 변환, z-index, 상단, 왼쪽, 너비, 높이에 대한 트랜지션 효과 */
}

/* 중앙 노드 스타일 */
#svg circle {
  fill: black;
}

.login-modal {
  /* 로그인 모달 스타일 */
}

.toggle-button {
  /* 토글 버튼 스타일 */
}

.side-tab {
  /* 사이드 탭 스타일 */
}
</style>

