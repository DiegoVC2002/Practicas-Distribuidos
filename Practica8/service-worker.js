const CACHE_NAME = 'carrera-autos-cache-v3';
const urlsToCache = [
  '/',
  'https://fonts.googleapis.com/css2?family=Orbitron:wght@600&display=swap',
  '/index.html',
  '/style.css',
  '/app.js',
  '/manifest.json',
  '/service-worker.js',
  '/icons/icon-192.png',
  '/icons/icon-512.png',
  '/img/auto1.png',
  '/img/auto2.png',
  '/img/auto3.png',
  '/img/auto4.png',
  '/img/auto5.png',
  '/img/auto6.png'
];

self.addEventListener('install', event => {
  event.waitUntil(
    caches.open(CACHE_NAME).then(cache => cache.addAll(urlsToCache))
  );
});

self.addEventListener('fetch', event => {
  event.respondWith(
    caches.match(event.request).then(response => response || fetch(event.request))
  );
});
