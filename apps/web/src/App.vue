<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue';
import { defaultSettings, normalizeSettings, type SiteSettings } from './settings';

type GuestbookEntry = {
  id: string;
  name: string;
  message: string;
  attending: boolean;
  createdAt: string;
};

const apiBaseUrl = import.meta.env.VITE_API_BASE_URL ?? 'http://localhost:8080';
const isAdminPage = window.location.pathname.startsWith('/admin');

const settings = reactive<SiteSettings>(structuredClone(defaultSettings));
const adminConfigured = ref(false);
const statusText = ref('');
const loading = ref(true);

const adminPassword = ref(sessionStorage.getItem('admin_password') ?? '');
const adminVerified = ref(false);
const setupPassword = ref('');

const guestbook = reactive({
  entries: [] as GuestbookEntry[],
  name: '',
  message: '',
  attending: false,
  status: '',
  submitting: false
});

const galleryStartIndex = ref(0);
const viewerOpen = ref(false);
const viewerIndex = ref(0);

const formattedDate = computed(() => {
  const date = new Date(`${settings.wedding.date}T00:00:00`);
  if (Number.isNaN(date.getTime())) return settings.wedding.date;
  return new Intl.DateTimeFormat('ko-KR', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
    weekday: 'long'
  }).format(date);
});

const weddingTimeText = computed(() => settings.wedding.time || '13:00');
const addressQuery = computed(() => encodeURIComponent(`${settings.wedding.address} ${settings.wedding.addressDetail}`.trim()));
const naverMapUrl = computed(() => `https://map.naver.com/v5/search/${addressQuery.value}`);
const kakaoMapUrl = computed(() => `https://map.kakao.com/link/search/${addressQuery.value}`);
const naverMapEmbedUrl = computed(() => `https://map.naver.com/v5/search/${addressQuery.value}`);
const invitationUrl = computed(() => settings.share.invitationUrl || window.location.href);

const weddingDayNumber = computed(() => Number(settings.wedding.date.split('-')[2]) || 0);

const calendarMatrix = computed(() => {
  const [year, month] = settings.wedding.date.split('-').map((v) => Number(v));
  if (!year || !month) return [] as Array<Array<number | null>>;

  const firstDay = new Date(year, month - 1, 1).getDay();
  const daysInMonth = new Date(year, month, 0).getDate();
  const cells: Array<number | null> = Array(firstDay).fill(null);
  for (let i = 1; i <= daysInMonth; i += 1) cells.push(i);
  while (cells.length % 7 !== 0) cells.push(null);

  const rows: Array<Array<number | null>> = [];
  for (let i = 0; i < cells.length; i += 7) rows.push(cells.slice(i, i + 7));
  return rows;
});

const galleryImages = computed(() => settings.media.galleryImages.filter(Boolean));
const visibleGalleryImages = computed(() => {
  const list = galleryImages.value;
  if (list.length === 0) return [] as string[];

  const count = Math.min(3, list.length);
  const visible: string[] = [];
  for (let i = 0; i < count; i += 1) {
    visible.push(list[(galleryStartIndex.value + i) % list.length]);
  }
  return visible;
});

const currentViewerImage = computed(() => {
  const list = galleryImages.value;
  if (!list.length) return '';
  return list[((viewerIndex.value % list.length) + list.length) % list.length];
});

const gallerySlots = computed(() => Array.from({ length: 10 }, (_, i) => i));

async function fetchSiteSettings() {
  const response = await fetch(`${apiBaseUrl}/api/v1/site-settings`);
  if (!response.ok) throw new Error('설정 정보를 불러오지 못했습니다.');
  const data = await response.json();
  Object.assign(settings, normalizeSettings(data.settings));
  adminConfigured.value = Boolean(data.adminConfigured);
}

async function loadGuestbook() {
  const response = await fetch(`${apiBaseUrl}/api/v1/guestbook`);
  if (!response.ok) throw new Error('방명록을 불러오지 못했습니다.');
  guestbook.entries = await response.json();
}

async function verifyAdminPassword(password: string) {
  const response = await fetch(`${apiBaseUrl}/api/v1/site-settings/admin/verify`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ password })
  });
  if (!response.ok) return false;
  const data = await response.json();
  adminConfigured.value = Boolean(data.adminConfigured);
  return Boolean(data.success);
}

async function setupAdminPasswordAndMove() {
  if (!setupPassword.value.trim()) {
    statusText.value = '비밀번호를 입력해주세요.';
    return;
  }

  const response = await fetch(`${apiBaseUrl}/api/v1/site-settings/admin/setup`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ password: setupPassword.value.trim() })
  });

  if (!response.ok) {
    statusText.value = '비밀번호 설정에 실패했습니다.';
    return;
  }

  sessionStorage.setItem('admin_password', setupPassword.value.trim());
  window.location.href = '/admin';
}

async function openAdminFromInvitation() {
  const password = window.prompt('관리자 비밀번호를 입력해주세요.\n(최초 1회는 새 비밀번호를 설정합니다.)')?.trim();
  if (!password) return;

  const unlocked = await verifyAdminPassword(password);
  if (unlocked) {
    sessionStorage.setItem('admin_password', password);
    window.location.href = '/admin';
    return;
  }

  if (!adminConfigured.value) {
    const setup = window.confirm('아직 관리자 비밀번호가 없습니다. 입력한 값으로 비밀번호를 생성할까요?');
    if (!setup) return;

    const setupResponse = await fetch(`${apiBaseUrl}/api/v1/site-settings/admin/setup`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ password })
    });

    if (setupResponse.ok) {
      sessionStorage.setItem('admin_password', password);
      window.location.href = '/admin';
      return;
    }
  }

  window.alert('비밀번호가 올바르지 않습니다.');
}

async function unlockAdminPage() {
  if (!adminPassword.value.trim()) {
    statusText.value = '비밀번호를 입력해주세요.';
    return;
  }

  const ok = await verifyAdminPassword(adminPassword.value.trim());
  if (!ok) {
    statusText.value = adminConfigured.value ? '비밀번호가 일치하지 않습니다.' : '먼저 비밀번호를 설정해주세요.';
    return;
  }

  sessionStorage.setItem('admin_password', adminPassword.value.trim());
  adminVerified.value = true;
  statusText.value = '관리자 인증 완료';
}

function updateNested(path: string, value: string) {
  const keys = path.split('.');
  let cursor: any = settings;
  for (let i = 0; i < keys.length - 1; i += 1) cursor = cursor[keys[i]];
  cursor[keys[keys.length - 1]] = value;
}

function onFileChange(event: Event, path: string) {
  const target = event.target as HTMLInputElement;
  const file = target.files?.[0];
  if (!file) return;

  const reader = new FileReader();
  reader.onload = () => updateNested(path, String(reader.result ?? ''));
  reader.readAsDataURL(file);
}

function onGalleryFileChange(event: Event, index: number) {
  const target = event.target as HTMLInputElement;
  const file = target.files?.[0];
  if (!file) return;

  const reader = new FileReader();
  reader.onload = () => {
    settings.media.galleryImages[index] = String(reader.result ?? '');
  };
  reader.readAsDataURL(file);
}

function clearGalleryImage(index: number) {
  settings.media.galleryImages[index] = '';
}

async function saveAdminSettings() {
  if (!adminPassword.value.trim()) {
    statusText.value = '저장 전 비밀번호를 입력해주세요.';
    return;
  }

  const response = await fetch(`${apiBaseUrl}/api/v1/site-settings/admin`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
      password: adminPassword.value.trim(),
      settings
    })
  });

  if (!response.ok) {
    statusText.value = '저장 실패: 비밀번호 또는 서버 상태를 확인해주세요.';
    return;
  }

  statusText.value = '저장되었습니다.';
}

function goInvitationWithRefresh() {
  window.location.href = `/?refresh=${Date.now()}`;
}

async function submitGuestbook() {
  if (!guestbook.name.trim() || !guestbook.message.trim()) {
    guestbook.status = '이름과 메시지를 입력해주세요.';
    return;
  }

  guestbook.submitting = true;
  guestbook.status = '';

  try {
    const response = await fetch(`${apiBaseUrl}/api/v1/guestbook`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        name: guestbook.name.trim(),
        message: guestbook.message.trim(),
        attending: guestbook.attending
      })
    });

    if (!response.ok) throw new Error();

    guestbook.name = '';
    guestbook.message = '';
    guestbook.attending = false;
    guestbook.status = '축하 메시지가 등록되었습니다.';
    await loadGuestbook();
  } catch {
    guestbook.status = '등록에 실패했습니다. 잠시 후 다시 시도해주세요.';
  } finally {
    guestbook.submitting = false;
  }
}

async function shareKakao() {
  const text = settings.share.kakaoMessage || `${settings.couple.groom} & ${settings.couple.bride} 결혼식에 초대합니다.`;

  if (navigator.share) {
    try {
      await navigator.share({ title: '모바일 청첩장', text, url: invitationUrl.value });
      return;
    } catch {
      // ignore
    }
  }

  await navigator.clipboard.writeText(invitationUrl.value);
  window.alert('공유 링크가 복사되었습니다. 카카오톡 대화창에 붙여넣어 공유해주세요.');
}

async function copyInvitationLink() {
  await navigator.clipboard.writeText(invitationUrl.value);
  window.alert('청첩장 링크를 복사했습니다.');
}

function moveGallery(step: number) {
  const list = galleryImages.value;
  if (!list.length) return;
  const next = (galleryStartIndex.value + step + list.length) % list.length;
  galleryStartIndex.value = next;
}

function openGalleryViewer(visibleIndex: number) {
  const list = galleryImages.value;
  if (!list.length) return;
  viewerIndex.value = (galleryStartIndex.value + visibleIndex) % list.length;
  viewerOpen.value = true;
}

function moveViewer(step: number) {
  const list = galleryImages.value;
  if (!list.length) return;
  viewerIndex.value = (viewerIndex.value + step + list.length) % list.length;
}

onMounted(async () => {
  try {
    await fetchSiteSettings();

    if (isAdminPage) {
      if (adminPassword.value) {
        adminVerified.value = await verifyAdminPassword(adminPassword.value);
      }
    } else {
      await loadGuestbook();
    }
  } catch {
    statusText.value = '데이터를 불러오지 못했습니다. API 실행 상태를 확인해주세요.';
  } finally {
    loading.value = false;
  }
});
</script>

<template>
  <main class="page" v-if="!loading">
    <template v-if="!isAdminPage">
      <section class="hero" :style="settings.media.heroImage ? { backgroundImage: `url(${settings.media.heroImage})` } : {}">
        <div class="hero-overlay">
          <div class="hero-chip">Wedding Invitation</div>
          <h1>
            <span class="hero-chip big">{{ settings.couple.groom }} & {{ settings.couple.bride }}</span>
          </h1>
          <p><span class="hero-chip">{{ formattedDate }} {{ weddingTimeText }}</span></p>
          <p><span class="hero-chip">{{ settings.wedding.venueName }}</span></p>
        </div>
      </section>

      <section class="card">
        <h2>인사말</h2>
        <p>{{ settings.greeting }}</p>
      </section>

      <section class="card couple-intro">
        <img v-if="settings.media.coupleImage" :src="settings.media.coupleImage" alt="부부 소개 이미지" />
        <div>
          <h2>신랑 · 신부 소개</h2>
          <p>신랑 {{ settings.couple.groom }} · 신부 {{ settings.couple.bride }}</p>
          <p>신랑측 {{ settings.couple.groomFather }} · {{ settings.couple.groomMother }}</p>
          <p>신부측 {{ settings.couple.brideFather }} · {{ settings.couple.brideMother }}</p>
        </div>
      </section>

      <section class="card">
        <h2>우리의 이야기</h2>
        <ul class="story-list">
          <li><strong>첫 만남</strong><span>{{ settings.story.firstMeet }}</span></li>
          <li><strong>프로포즈</strong><span>{{ settings.story.proposal }}</span></li>
          <li><strong>결혼식</strong><span>{{ settings.story.weddingDay }}</span></li>
        </ul>
      </section>

      <section class="card">
        <h2>예식 달력</h2>
        <p>{{ formattedDate }}</p>
        <table class="calendar">
          <thead>
            <tr>
              <th>일</th>
              <th>월</th>
              <th>화</th>
              <th>수</th>
              <th>목</th>
              <th>금</th>
              <th>토</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(week, index) in calendarMatrix" :key="index">
              <td v-for="(day, dayIndex) in week" :key="dayIndex">
                <span v-if="day" :class="{ marked: day === weddingDayNumber }">
                  <template v-if="day === weddingDayNumber">
                    <span class="heart">♥</span>{{ day }}
                  </template>
                  <template v-else>
                    {{ day }}
                  </template>
                </span>
              </td>
            </tr>
          </tbody>
        </table>
      </section>

      <section class="card">
        <h2>오시는 길</h2>
        <div class="map-wrap">
          <iframe :src="naverMapEmbedUrl" title="네이버 지도" loading="lazy"></iframe>
        </div>
        <p>{{ settings.wedding.venueName }}</p>
        <p>{{ settings.wedding.address }}</p>
        <p>{{ settings.wedding.addressDetail }}</p>
        <div class="button-row">
          <a :href="naverMapUrl" target="_blank" rel="noreferrer">네이버 지도 열기</a>
          <a :href="kakaoMapUrl" target="_blank" rel="noreferrer">카카오맵 열기</a>
        </div>
      </section>

      <section class="card">
        <h2>갤러리</h2>
        <div class="gallery-controls" v-if="galleryImages.length > 1">
          <button type="button" @click="moveGallery(-1)">이전</button>
          <button type="button" @click="moveGallery(1)">다음</button>
        </div>
        <div class="gallery-grid" v-if="galleryImages.length">
          <button class="gallery-item" type="button" v-for="(img, idx) in visibleGalleryImages" :key="`${img}-${idx}`" @click="openGalleryViewer(idx)">
            <img :src="img" :alt="`갤러리 ${idx + 1}`" />
          </button>
        </div>
        <p class="small" v-else>등록된 갤러리 이미지가 없습니다.</p>
      </section>

      <section class="card" v-if="settings.media.videoUrl">
        <h2>영상</h2>
        <div class="video-wrap">
          <iframe :src="settings.media.videoUrl" title="wedding video" allowfullscreen></iframe>
        </div>
      </section>

      <section class="card">
        <h2>연락처</h2>
        <p>신랑: {{ settings.contact.groomPhone || '미입력' }}</p>
        <p>신부: {{ settings.contact.bridePhone || '미입력' }}</p>
      </section>

      <section class="card">
        <h2>마음 전하실 곳</h2>
        <p>신랑측: {{ settings.accounts.groom }}</p>
        <p>신부측: {{ settings.accounts.bride }}</p>
      </section>

      <section class="card">
        <h2>공유하기</h2>
        <div class="button-row">
          <button type="button" @click="shareKakao">카톡 공유</button>
          <button type="button" @click="copyInvitationLink">청첩장 링크 복사</button>
        </div>
        <p class="small">{{ invitationUrl }}</p>
      </section>

      <section class="card">
        <h2>축하 메시지</h2>
        <div class="form-grid">
          <input v-model="guestbook.name" type="text" maxlength="40" placeholder="이름" />
          <textarea v-model="guestbook.message" maxlength="400" rows="4" placeholder="축하 메시지"></textarea>
          <label class="checkbox">
            <input v-model="guestbook.attending" type="checkbox" /> 참석 예정입니다
          </label>
          <button :disabled="guestbook.submitting" type="button" @click="submitGuestbook">
            {{ guestbook.submitting ? '등록 중...' : '메시지 남기기' }}
          </button>
        </div>
        <p class="status">{{ guestbook.status }}</p>
      </section>

      <section class="card">
        <h2>방명록</h2>
        <ul class="entries">
          <li class="entry" v-for="entry in guestbook.entries" :key="entry.id">
            <div class="entry-head">
              <strong>{{ entry.name }}</strong>
              <span>{{ new Date(entry.createdAt).toLocaleString('ko-KR') }}</span>
            </div>
            <p>{{ entry.message }}</p>
            <small v-if="entry.attending">참석 예정</small>
          </li>
        </ul>
      </section>

      <section class="admin-entry">
        <button type="button" @click="openAdminFromInvitation">관리자 페이지</button>
      </section>

      <div class="modal" v-if="viewerOpen" @click.self="viewerOpen = false">
        <button class="modal-close" @click="viewerOpen = false" type="button">닫기</button>
        <button class="modal-nav left" @click="moveViewer(-1)" type="button">‹</button>
        <img class="modal-image" :src="currentViewerImage" alt="확대 이미지" />
        <button class="modal-nav right" @click="moveViewer(1)" type="button">›</button>
      </div>
    </template>

    <template v-else>
      <section class="card admin-card" v-if="!adminConfigured">
        <h2>관리자 비밀번호 최초 설정</h2>
        <input v-model="setupPassword" type="password" placeholder="관리자 비밀번호" />
        <button type="button" @click="setupAdminPasswordAndMove">비밀번호 설정 후 관리자 페이지 이동</button>
        <p class="status">{{ statusText }}</p>
      </section>

      <section class="card admin-card" v-else-if="!adminVerified">
        <h2>관리자 로그인</h2>
        <input v-model="adminPassword" type="password" placeholder="관리자 비밀번호" />
        <button type="button" @click="unlockAdminPage">관리자 페이지 열기</button>
        <p class="status">{{ statusText }}</p>
      </section>

      <section class="card admin-card" v-else>
        <h2>관리자 페이지</h2>
        <p class="small">저장하면 모바일 청첩장에 즉시 반영됩니다.</p>
        <div class="button-row admin-top-actions">
          <button type="button" @click="saveAdminSettings">설정 저장</button>
          <button type="button" @click="goInvitationWithRefresh">청첩장으로 돌아가기</button>
        </div>

        <h3>기본 정보</h3>
        <div class="form-grid">
          <input :value="settings.couple.groom" @input="updateNested('couple.groom', ($event.target as HTMLInputElement).value)" placeholder="신랑 이름" />
          <input :value="settings.couple.bride" @input="updateNested('couple.bride', ($event.target as HTMLInputElement).value)" placeholder="신부 이름" />
          <input :value="settings.couple.groomFather" @input="updateNested('couple.groomFather', ($event.target as HTMLInputElement).value)" placeholder="신랑 아버지" />
          <input :value="settings.couple.groomMother" @input="updateNested('couple.groomMother', ($event.target as HTMLInputElement).value)" placeholder="신랑 어머니" />
          <input :value="settings.couple.brideFather" @input="updateNested('couple.brideFather', ($event.target as HTMLInputElement).value)" placeholder="신부 아버지" />
          <input :value="settings.couple.brideMother" @input="updateNested('couple.brideMother', ($event.target as HTMLInputElement).value)" placeholder="신부 어머니" />
          <textarea :value="settings.greeting" @input="updateNested('greeting', ($event.target as HTMLTextAreaElement).value)" rows="3" placeholder="인삿말" />
        </div>

        <h3>예식/장소 정보</h3>
        <div class="form-grid">
          <input :value="settings.wedding.date" @input="updateNested('wedding.date', ($event.target as HTMLInputElement).value)" type="date" />
          <input :value="settings.wedding.time" @input="updateNested('wedding.time', ($event.target as HTMLInputElement).value)" type="time" />
          <input :value="settings.wedding.venueName" @input="updateNested('wedding.venueName', ($event.target as HTMLInputElement).value)" placeholder="예식장 이름" />
          <input :value="settings.wedding.address" @input="updateNested('wedding.address', ($event.target as HTMLInputElement).value)" placeholder="주소(지도 검색 기준)" />
          <input :value="settings.wedding.addressDetail" @input="updateNested('wedding.addressDetail', ($event.target as HTMLInputElement).value)" placeholder="상세 주소" />
        </div>

        <h3>연락처/계좌/공유</h3>
        <div class="form-grid">
          <input :value="settings.contact.groomPhone" @input="updateNested('contact.groomPhone', ($event.target as HTMLInputElement).value)" placeholder="신랑 연락처" />
          <input :value="settings.contact.bridePhone" @input="updateNested('contact.bridePhone', ($event.target as HTMLInputElement).value)" placeholder="신부 연락처" />
          <input :value="settings.accounts.groom" @input="updateNested('accounts.groom', ($event.target as HTMLInputElement).value)" placeholder="신랑측 계좌" />
          <input :value="settings.accounts.bride" @input="updateNested('accounts.bride', ($event.target as HTMLInputElement).value)" placeholder="신부측 계좌" />
          <input :value="settings.share.invitationUrl" @input="updateNested('share.invitationUrl', ($event.target as HTMLInputElement).value)" placeholder="공유용 청첩장 링크" />
          <input :value="settings.share.kakaoMessage" @input="updateNested('share.kakaoMessage', ($event.target as HTMLInputElement).value)" placeholder="카톡 공유 메시지" />
        </div>

        <h3>사진 섹션 업로드</h3>
        <div class="form-grid">
          <label>메인 첫 화면 이미지<input type="file" accept="image/*" @change="onFileChange($event, 'media.heroImage')" /></label>
          <label>부부 소개 이미지<input type="file" accept="image/*" @change="onFileChange($event, 'media.coupleImage')" /></label>
          <label>예식장 소개 이미지(옵션)<input type="file" accept="image/*" @change="onFileChange($event, 'media.venueImage')" /></label>

          <div class="gallery-admin-grid">
            <div class="gallery-slot" v-for="slot in gallerySlots" :key="slot">
              <label>갤러리 {{ slot + 1 }}
                <input type="file" accept="image/*" @change="onGalleryFileChange($event, slot)" />
              </label>
              <div class="slot-actions">
                <small>{{ settings.media.galleryImages[slot] ? '등록됨' : '비어있음' }}</small>
                <button type="button" @click="clearGalleryImage(slot)">지우기</button>
              </div>
            </div>
          </div>

          <input :value="settings.media.videoUrl" @input="updateNested('media.videoUrl', ($event.target as HTMLInputElement).value)" placeholder="영상 URL (YouTube embed 등)" />
        </div>

        <h3>추가 스토리 섹션</h3>
        <div class="form-grid">
          <textarea :value="settings.story.firstMeet" @input="updateNested('story.firstMeet', ($event.target as HTMLTextAreaElement).value)" rows="2" placeholder="첫 만남" />
          <textarea :value="settings.story.proposal" @input="updateNested('story.proposal', ($event.target as HTMLTextAreaElement).value)" rows="2" placeholder="프로포즈" />
          <textarea :value="settings.story.weddingDay" @input="updateNested('story.weddingDay', ($event.target as HTMLTextAreaElement).value)" rows="2" placeholder="결혼식" />
        </div>

        <button type="button" @click="saveAdminSettings">설정 저장</button>
        <p class="status">{{ statusText }}</p>
      </section>
    </template>
  </main>

  <main class="page" v-else>
    <section class="card">
      <h2>로딩 중</h2>
    </section>
  </main>
</template>
