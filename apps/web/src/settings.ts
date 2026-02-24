export type SiteSettings = {
  greeting: string;
  wedding: {
    date: string;
    time: string;
    venueName: string;
    address: string;
    addressDetail: string;
  };
  couple: {
    groom: string;
    bride: string;
    groomFather: string;
    groomMother: string;
    brideFather: string;
    brideMother: string;
  };
  contact: {
    groomPhone: string;
    bridePhone: string;
  };
  accounts: {
    groom: string;
    bride: string;
  };
  media: {
    heroImage: string;
    coupleImage: string;
    venueImage: string;
    galleryImages: string[];
    videoUrl: string;
  };
  share: {
    invitationUrl: string;
    kakaoMessage: string;
  };
  story: {
    firstMeet: string;
    proposal: string;
    weddingDay: string;
  };
};

export const defaultSettings: SiteSettings = {
  greeting:
    '두 사람이 사랑으로 만나 하나의 가정을 이루려 합니다. 소중한 걸음으로 축복해 주시면 감사하겠습니다.',
  wedding: {
    date: '2026-05-30',
    time: '13:00',
    venueName: '모바일웨딩홀 2층 그레이스홀',
    address: '서울시 중구 세종대로 110',
    addressDetail: '2층 그레이스홀'
  },
  couple: {
    groom: '민수',
    bride: '지은',
    groomFather: '김아버지',
    groomMother: '박어머니',
    brideFather: '이아버지',
    brideMother: '최어머니'
  },
  contact: {
    groomPhone: '',
    bridePhone: ''
  },
  accounts: {
    groom: '신랑측 계좌를 입력하세요',
    bride: '신부측 계좌를 입력하세요'
  },
  media: {
    heroImage: '',
    coupleImage: '',
    venueImage: '',
    galleryImages: Array.from({ length: 10 }, () => ''),
    videoUrl: ''
  },
  share: {
    invitationUrl: '',
    kakaoMessage: '저희 결혼식에 초대합니다.'
  },
  story: {
    firstMeet: '처음 만난 날의 추억을 적어주세요.',
    proposal: '프로포즈 이야기를 적어주세요.',
    weddingDay: '결혼식 날 전하고 싶은 말을 적어주세요.'
  }
};

function normalizeGalleryArray(input: unknown): string[] {
  const base = Array.from({ length: 10 }, () => '');
  if (Array.isArray(input)) {
    for (let i = 0; i < Math.min(10, input.length); i += 1) {
      base[i] = typeof input[i] === 'string' ? input[i] : '';
    }
  }
  return base;
}

export function normalizeSettings(raw: unknown): SiteSettings {
  if (!raw || typeof raw !== 'object') {
    return structuredClone(defaultSettings);
  }

  const input = raw as Record<string, any>;
  const legacyGallery = [
    input?.media?.gallery1,
    input?.media?.gallery2,
    input?.media?.gallery3
  ].filter((item: unknown) => typeof item === 'string');

  const galleryImages = normalizeGalleryArray(
    input?.media?.galleryImages && Array.isArray(input.media.galleryImages)
      ? input.media.galleryImages
      : legacyGallery
  );

  return {
    greeting: input.greeting ?? defaultSettings.greeting,
    wedding: { ...defaultSettings.wedding, ...(input.wedding ?? {}) },
    couple: { ...defaultSettings.couple, ...(input.couple ?? {}) },
    contact: { ...defaultSettings.contact, ...(input.contact ?? {}) },
    accounts: { ...defaultSettings.accounts, ...(input.accounts ?? {}) },
    media: {
      ...defaultSettings.media,
      ...(input.media ?? {}),
      galleryImages
    },
    share: { ...defaultSettings.share, ...(input.share ?? {}) },
    story: { ...defaultSettings.story, ...(input.story ?? {}) }
  };
}
