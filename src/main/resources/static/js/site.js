(function () {
  var header = document.querySelector(".app-bar");
  if (!header) return;

  function updateHeader() {
    if (window.scrollY > 24) {
      header.classList.add("scrolled");
    } else {
      header.classList.remove("scrolled");
    }
  }

  updateHeader();
  window.addEventListener("scroll", updateHeader, { passive: true });
})();
