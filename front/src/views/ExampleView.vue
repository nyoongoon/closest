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
  </div>
</template>

<script lang="ts">
import { defineComponent, reactive, onMounted } from 'vue';

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

    onMounted(() => {
      startMovement();
    });

    return {
      centerNode,
      centerNodeSize,
      nodeSize,
      nodes,
      handleMouseOver,
      handleMouseLeave,
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
</style>
