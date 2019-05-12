import {Component} from "@angular/core";

@Component({
  selector: "app-root",
  templateUrl: "./app.component.html",
  styleUrls: ["./app.component.scss"]
})
export class AppComponent {
  title = "exchange-ui";
  convertFrom = "eur";
  convertInto = "hrk";
  amountFrom = 1;
  amountInto = 7.44;
}
