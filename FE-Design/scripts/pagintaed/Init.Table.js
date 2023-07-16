import { getUserId, parseJwt } from "../../utils/tokenUtils.js";
import { MY_INVOICES_URL } from "../../settings/settings.js";
import { editItem, deleteItem, goToNewPageWithID } from "./main.js";

const token = localStorage.getItem("token");
const {role} = parseJwt();

if(!token)
    window.location.href = "../../pages/login.html";

if(role == "USER")
    $('.display').css('display', 'none');

if(role == "AUDITOR")
    window.location.href = "../../pages/invoices2.html";

$(document).ready(() => {

    var table = $("#myTable").DataTable();

    $.ajax({
        url: `${MY_INVOICES_URL}/${getUserId()}`,
        method: "GET",
        dataType: "json",
        headers: { Authorization: "Bearer " + localStorage.getItem("token") },
        success: (invoices) => {

            invoices.forEach((item) => {
                var previewButton = '<button type="button" style="font-size: 13px;padding: 0.25rem 0.5rem" class="btn btn-danger btn-preview">Preview</button>';
                var editButton = '<button type="button" style="font-size: 14px;padding: 0.25rem 0.5rem;" class="btn btn-primary btn-edit modify">Edit</button>';
                var deleteButton = '<button type="button" style="font-size: 14px;padding: 0.25rem 0.5rem;" class="btn btn-danger btn-delete modify">Delete</button>';

                var rowNode = table.row.add([item.id, item.user.username, item.createdAt, item.totalPrice, "", ""]).draw().node();

                $(rowNode).find("td:nth-last-child(2)").html('<div class="btn-group">' + previewButton + '</div>');
                $(rowNode).find("td:nth-last-child(1)").html('<div class="btn-group">' + editButton + deleteButton + '</div>');

                $(rowNode).find(".btn-edit").on("click", () => {
                    editItem(item.id);
                });

                $(rowNode).find(".btn-delete").on("click", () => {
                    deleteItem(item.id);
                });

                $(rowNode).find(".btn-preview").on("click", () => {
                    goToNewPageWithID(item);
                });
            });
        },
        error: (err) => {
            console.log(err.responseText);
        },
    });
});
