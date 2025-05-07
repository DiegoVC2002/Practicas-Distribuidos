let autos = [];
let posiciones = [];
let intervalo = null;
function registrarAuto() {
  const input = document.getElementById('nombreAuto');
  const nombre = input.value.trim();
  const mensaje = document.getElementById('mensaje');

  if (!nombre) {
    mensaje.innerText = 'Ponle nombre al auto 🏎️';
    return;
  }

  if (autos.length >= 6) {
    mensaje.innerText = 'Máximo 6 autos permitidos';
    return;
  }

  autos.push({ nombre, id: autos.length });
  mensaje.innerText = '';

  input.value = '';
  renderizarAutos();
}
function renderizarAutos() {
  const pista = document.getElementById('pista');
  const contenedor = document.getElementById('autos');
  contenedor.innerHTML = '';

  autos.forEach((auto, index) => {
    const carril = document.createElement('div');
    carril.className = 'carril';

    const nombre = document.createElement('div');
    nombre.className = 'nombre-auto';
    nombre.innerText = auto.nombre;

    const imagen = document.createElement('div');
    imagen.className = 'carro';
    imagen.id = `auto-${index}`;
    imagen.innerHTML = `<img src="img/auto${(index % 6) + 1}.png" alt="auto" />`;

    const meta = document.createElement('div');
    meta.className = 'meta';

    carril.appendChild(nombre);
    carril.appendChild(imagen);
    carril.appendChild(meta);
    contenedor.appendChild(carril);
  });

  pista.classList.remove('hide');
}
function iniciarCarrera() {
  if (autos.length < 4) {
    alert('Se necesitan al menos 4 autos para iniciar la carrera.');
    return;
  }

  posiciones = [];
  const meta = document.querySelector('.carril').offsetWidth - 60;

  intervalo = setInterval(() => {
    autos.forEach((auto, i) => {
      if (posiciones.includes(auto)) return;

      const img = document.getElementById(`auto-${i}`);
      let pos = parseInt(img.style.left) || 0;
      let avance = Math.floor(Math.random() * 10) + 5;
      pos += avance;

      if (pos >= meta) {
        pos = meta;
        posiciones.push(auto);
        if (posiciones.length === autos.length) {
          clearInterval(intervalo);
          mostrarPodio();
        }
      }

      img.style.left = `${pos}px`;
    });
  }, 100);
}
function mostrarPodio() {
  document.getElementById('pista').classList.add('hide');
  const podio = document.getElementById('podio');
  const lista = document.getElementById('resultado');
  lista.innerHTML = '';
  posiciones.forEach(auto => {
    const li = document.createElement('li');
    li.innerText = auto.nombre;
    lista.appendChild(li);
  });
  podio.classList.remove('hide');
}
function reiniciarCarrera() {
  autos = [];
  posiciones = [];
  document.getElementById('autos').innerHTML = '';
  document.getElementById('registro').classList.remove('hide');
  document.getElementById('pista').classList.add('hide');
  document.getElementById('podio').classList.add('hide');
}
