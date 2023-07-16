import { ALL_LOGS_URL } from "../../settings/settings.js";
import { parseJwt } from "../../utils/tokenUtils.js";
import { editItem, deleteItem } from "./main.js";

const token = localStorage.getItem("token");
const {role} = parseJwt();

if(!token)
    window.location.href = "../../pages/login.html"

if(role == "USER")
    window.location.href = "../../pages/pagintaed.html"

if(role == "AUDITOR"){
    window.location.href = "../../pages/invoices2.html"
}

$(document).ready(() => {

    var table = $("#myTable").DataTable();

    $.ajax({
        url: ALL_LOGS_URL,
        type: "GET",
        dataType: "json",
        headers: { Authorization: "Bearer " + localStorage.getItem("token") },
        success: (logs) => {

            logs.forEach((log) => {
                var editButton = '<button type="button" style="font-size: 14px;padding: 0.25rem 0.5rem;" class="btn btn-primary btn-edit">Edit</button>';
                var deleteButton = '<button type="button" style="font-size: 14px;padding: 0.25rem 0.5rem;" class="btn btn-danger btn-delete">Delete</button>';

                var rowNode = table.row.add([log.id, log.action, log.createdAt, log.user.username, log.description, ""]).draw().node();

                $(rowNode).find("td:last-child").html('<div class="btn-group">' + editButton + deleteButton + '</div>');

                $(rowNode).find(".btn-edit").on("click", () => {
                    editItem(log.id);
                });

                $(rowNode).find(".btn-delete").on("click", () => {
                    deleteItem(log.id);
                });

            });
        },
        error: (err) => {
            console.log(err);
        },
    });
});
