import { DELAY, LOGIN_URL } from "../../settings/settings.js";

let token = localStorage.getItem("token");

if (token) {
  window.location.href = "../../pages/pagintaed.html";
}

$("#login-form").on("submit", loginUser);

function loginUser(e) {
  e.preventDefault();
  const form = e.target;
  const user = {
    username: form.username.value,
    password: form.password.value,
  };
  $.ajax({
    url: LOGIN_URL,
    type: "POST",
    data: JSON.stringify(user),
    contentType: "application/json",
    success: function (response) {
      localStorage.setItem("token", response)
      window.location.href = "../../pages/pagintaed.html"
    },
    error: function (err) {
      const message = `<div class="alert alert-danger" role="alert">${err.responseText}</div>`;
      $("#error").append(message);
      setTimeout(() => {
        $("#error").empty();
      }, DELAY);
    },
  });
}
