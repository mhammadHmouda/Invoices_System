import { getUserId, parseJwt } from "../../utils/tokenUtils.js";
import { USERS_URL } from "../../settings/settings.js";
import { editUser, deleteUser } from "./main.js";

const token = localStorage.getItem("token");
const {role} = parseJwt();

if(!token)
    window.location.href = "../../pages/login.html";

if(role != "SUPERUSER")
    $('.displayusers').css('display', 'none');

if(role == "USER"){
    $('.display').css('display', 'none');
    window.location.href = "../../pages/pagintaed.html";
}

if(role == "AUDITOR")
    window.location.href = "../../pages/invoices2.html";

$(document).ready(() => {

    var table = $("#myTable").DataTable();

    $.ajax({
        url: USERS_URL,
        method: "GET",
        dataType: "json",
        headers: { Authorization: "Bearer " + localStorage.getItem("token") },
        success: (users) => {

            users.forEach((user) => {
                var previewButton = '<button type="button" style="font-size: 13px;padding: 0.25rem 0.5rem" class="btn btn-danger btn-preview">Preview</button>';
                var editButton = '<button type="button" style="font-size: 14px;padding: 0.25rem 0.5rem;" class="btn btn-primary btn-edit modify">Edit</button>';
                var deleteButton = '<button type="button" style="font-size: 14px;padding: 0.25rem 0.5rem;" class="btn btn-danger btn-delete modify">Delete</button>';

                var rowNode = table.row.add([user.id, user.username, user.email, user.role, ""]).draw().node();

                $(rowNode).find("td:nth-last-child(1)").html('<div class="btn-group">' + editButton + deleteButton + '</div>');

                $(rowNode).find(".btn-edit").on("click", () => {
                    editUser(user.id);
                });

                $(rowNode).find(".btn-delete").on("click", () => {
                    deleteUser(user.id);
                });
            });
        },
        error: (err) => {
            console.log(err.responseText);
        },
    });
});
