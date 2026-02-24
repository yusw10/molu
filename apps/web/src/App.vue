<script setup lang="ts">
import { onMounted, ref } from 'vue';

type GuestbookEntry = {
  id: string;
  name: string;
  message: string;
  attending: boolean;
  createdAt: string;
};

const apiBaseUrl = import.meta.env.VITE_API_BASE_URL ?? 'http://localhost:8080';

const entries = ref<GuestbookEntry[]>([]);
const name = ref('');
const message = ref('');
const attending = ref(false);
const isLoading = ref(false);
const statusText = ref('');

async function loadEntries() {
  const response = await fetch(`${apiBaseUrl}/api/v1/guestbook`);
  if (!response.ok) {
    throw new Error('Failed to load guestbook entries');
  }
  entries.value = await response.json();
}

async function submitGuestbook() {
  if (!name.value.trim() || !message.value.trim()) {
    statusText.value = '이름과 메시지를 입력해주세요.';
    return;
  }

  isLoading.value = true;
  statusText.value = '';

  try {
    const response = await fetch(`${apiBaseUrl}/api/v1/guestbook`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        name: name.value.trim(),
        message: message.value.trim(),
        attending: attending.value
      })
    });

    if (!response.ok) {
      throw new Error('Submit failed');
    }

    name.value = '';
    message.value = '';
    attending.value = false;
    statusText.value = '축하 메시지가 등록되었습니다.';
    await loadEntries();
  } catch {
    statusText.value = '등록에 실패했습니다. 잠시 후 다시 시도해주세요.';
  } finally {
    isLoading.value = false;
  }
}

onMounted(async () => {
  try {
    await loadEntries();
  } catch {
    statusText.value = '방명록을 불러오지 못했습니다.';
  }
});
</script>

<template>
  <main class="page">
    <section class="hero card">
      <p class="label">Wedding Invitation</p>
      <h1>민수 & 지은</h1>
      <p>2026.05.30 Sat 1:00 PM</p>
      <p>서울 어딘가 웨딩홀</p>
    </section>

    <section class="card highlight">
      <h2>인사말</h2>
      <p>
        소중한 분들을 모시고 새로운 시작을 함께하고자 합니다.
        바쁘시더라도 오셔서 축복해 주시면 감사하겠습니다.
      </p>
    </section>

    <section class="card">
      <h2>축하 메시지</h2>
      <div class="form-grid">
        <input v-model="name" type="text" maxlength="40" placeholder="이름" />
        <textarea v-model="message" maxlength="400" rows="4" placeholder="축하 메시지"></textarea>
        <label class="checkbox">
          <input v-model="attending" type="checkbox" /> 참석 예정입니다
        </label>
        <button :disabled="isLoading" @click="submitGuestbook">
          {{ isLoading ? '등록 중...' : '메시지 남기기' }}
        </button>
      </div>
      <p class="status">{{ statusText }}</p>
    </section>

    <section class="card">
      <h2>방명록</h2>
      <ul class="entries">
        <li v-for="entry in entries" :key="entry.id" class="entry">
          <div class="entry-head">
            <strong>{{ entry.name }}</strong>
            <span>{{ new Date(entry.createdAt).toLocaleString('ko-KR') }}</span>
          </div>
          <p>{{ entry.message }}</p>
          <small v-if="entry.attending">참석 예정</small>
        </li>
      </ul>
    </section>
  </main>
</template>
