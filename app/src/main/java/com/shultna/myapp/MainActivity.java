// لا تنسَ إبقاء سطر الـ package الخاص بمشروعك هنا في الأعلى

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class MainActivity extends Activity {

    // قائمة الخدمات والأسعار
    static class ServiceItem {
        String title;
        String subtitle;
        double price;
        String requiredInputHint;
        String colorHex;

        ServiceItem(String title, String subtitle, double price, String requiredInputHint, String colorHex) {
            this.title = title;
            this.subtitle = subtitle;
            this.price = price;
            this.requiredInputHint = requiredInputHint;
            this.colorHex = colorHex;
        }
    }

    // عنصر داخل السلة
    static class CartOrder {
        ServiceItem service;
        String userTargetInfo; // ID أو الحساب المراد شحنه

        CartOrder(ServiceItem service, String userTargetInfo) {
            this.service = service;
            this.userTargetInfo = userTargetInfo;
        }
    }

    private final ArrayList<CartOrder> cart = new ArrayList<>();
    private Button cartButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // الواجهة الرئيسية
        LinearLayout rootLayout = new LinearLayout(this);
        rootLayout.setOrientation(LinearLayout.VERTICAL);
        rootLayout.setBackgroundColor(Color.parseColor("#F4F6F9"));
        rootLayout.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        // الشريط العلوي (Header)
        LinearLayout header = new LinearLayout(this);
        header.setOrientation(LinearLayout.HORIZONTAL);
        header.setBackgroundColor(Color.parseColor("#1E293B"));
        header.setPadding(35, 45, 35, 45);
        header.setGravity(Gravity.CENTER_VERTICAL);

        TextView storeTitle = new TextView(this);
        storeTitle.setText("متجر الخدمات الرقمية الذكي");
        storeTitle.setTextColor(Color.WHITE);
        storeTitle.setTextSize(19);
        storeTitle.setTypeface(null, Typeface.BOLD);
        LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        storeTitle.setLayoutParams(titleParams);

        cartButton = new Button(this);
        updateCartBadge();
        cartButton.setBackgroundColor(Color.parseColor("#10B981"));
        cartButton.setTextColor(Color.WHITE);
        cartButton.setOnClickListener(v -> showCartDialog());

        header.addView(storeTitle);
        header.addView(cartButton);
        rootLayout.addView(header);

        // قائمة الخدمات المطلوبة
        ServiceItem[] services = new ServiceItem[]{
            new ServiceItem("تيليجرام مميز (Telegram Premium)", "اشتراك 3 أشهر تفعيل فوري", 12.0, "أدخل معرف التيليجرام (@username):", "#0088CC"),
            new ServiceItem("شراء نجوم تيليجرام (Stars)", "باقة 250 نجمة لدعم القنوات والبوتات", 6.0, "أدخل معرف حسابك في تيليجرام (@username):", "#F59E0B"),
            new ServiceItem("دفع رسوم تفعيل حساب تيليجرام", "سداد رسوم التوثيق والتفعيل الرسمي", 3.5, "أدخل رقم الهاتف المرتبط بالحساب:", "#0284C7"),
            new ServiceItem("شحن شدات ببجي (PUBG UC)", "باقة 660 شدة شحن فوري عبر المعرف", 10.0, "أدخل الـ ID الخاص بك في لعبة ببجي:", "#D97706"),
            new ServiceItem("شحن عملات تيك توك (TikTok Coins)", "باقة 700 عملة لدعم البث المباشر", 9.0, "أدخل اسم المستخدم في تيك توك:", "#111827"),
            new ServiceItem("اشتراك Gemini Advanced (جيمي ناي)", "حساب برو شهر كامل مع مساحة 2TB", 20.0, "أدخل البريد الإلكتروني (Gmail) للتفعيل:", "#4F46E5"),
            new ServiceItem("اشتراك Claude Pro (كلاود)", "تفعيل باقة الذكاء الاصطناعي الاحترافية (شهر)", 20.0, "أدخل البريد الإلكتروني الخاص بك:", "#D97757"),
            new ServiceItem("اشتراك ChatGPT Plus (شات جي بي تي)", "تفعيل GPT-4o باقة البلس الشهرية", 20.0, "أدخل البريد الإلكتروني لحساب OpenAI:", "#10A37F")
        };

        // حاوية التمرير لعرض البطاقات
        ScrollView scrollView = new ScrollView(this);
        LinearLayout listContainer = new LinearLayout(this);
        listContainer.setOrientation(LinearLayout.VERTICAL);
        listContainer.setPadding(25, 25, 25, 25);

        for (ServiceItem item : services) {
            listContainer.addView(createServiceCard(item));
        }

        scrollView.addView(listContainer);
        rootLayout.addView(scrollView);

        setContentView(rootLayout);
    }

    // تصميم بطاقة كل خدمة
    private View createServiceCard(ServiceItem item) {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(35, 35, 35, 35);

        GradientDrawable shape = new GradientDrawable();
        shape.setColor(Color.WHITE);
        shape.setCornerRadius(24f);
        shape.setStroke(3, Color.parseColor("#E2E8F0"));
        card.setBackground(shape);

        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        cardParams.setMargins(0, 0, 0, 28);
        card.setLayoutParams(cardParams);

        // اسم الخدمة
        TextView title = new TextView(this);
        title.setText(item.title);
        title.setTextSize(17);
        title.setTypeface(null, Typeface.BOLD);
        title.setTextColor(Color.parseColor(item.colorHex));

        // وصف الخدمة
        TextView subtitle = new TextView(this);
        subtitle.setText(item.subtitle);
        subtitle.setTextSize(14);
        subtitle.setTextColor(Color.parseColor("#64748B"));
        subtitle.setPadding(0, 8, 0, 20);

        // السعر وزر الطلب
        LinearLayout bottomRow = new LinearLayout(this);
        bottomRow.setOrientation(LinearLayout.HORIZONTAL);
        bottomRow.setGravity(Gravity.CENTER_VERTICAL);

        TextView price = new TextView(this);
        price.setText("السعر: $" + item.price);
        price.setTextSize(16);
        price.setTypeface(null, Typeface.BOLD);
        price.setTextColor(Color.parseColor("#0F172A"));
        LinearLayout.LayoutParams priceParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        price.setLayoutParams(priceParams);

        Button orderBtn = new Button(this);
        orderBtn.setText("اطلب الآن");
        orderBtn.setTextColor(Color.WHITE);

        GradientDrawable btnShape = new GradientDrawable();
        btnShape.setColor(Color.parseColor(item.colorHex));
        btnShape.setCornerRadius(16f);
        orderBtn.setBackground(btnShape);
        orderBtn.setPadding(35, 15, 35, 15);

        orderBtn.setOnClickListener(v -> showInputDialog(item));

        bottomRow.addView(price);
        bottomRow.addView(orderBtn);

        card.addView(title);
        card.addView(subtitle);
        card.addView(bottomRow);

        return card;
    }

    // نافذة إدخال معلومات الحساب (ID أو يوزر أو إيميل)
    private void showInputDialog(ServiceItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(item.title);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 30, 50, 10);

        TextView label = new TextView(this);
        label.setText(item.requiredInputHint);
        label.setTextSize(15);
        label.setPadding(0, 0, 0, 15);

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setHint("اكتب البيانات هنا بدقة...");

        layout.addView(label);
        layout.addView(input);
        builder.setView(layout);

        builder.setPositiveButton("إضافة للسلة", (dialog, which) -> {
            String userInfo = input.getText().toString().trim();
            if (userInfo.isEmpty()) {
                Toast.makeText(MainActivity.this, "يرجى إدخال البيانات المطلوبة لإتمام الشحن!", Toast.LENGTH_LONG).show();
            } else {
                cart.add(new CartOrder(item, userInfo));
                updateCartBadge();
                Toast.makeText(MainActivity.this, "تمت الإضافة إلى السلة بنجاح", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("إلغاء", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    // تحديث عداد السلة
    private void updateCartBadge() {
        cartButton.setText("السلة (" + cart.size() + ")");
    }

    // نافذة مراجعة السلة وإتمام الدفع
    private void showCartDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("سلة المشتريات والطلبات");

        if (cart.isEmpty()) {
            builder.setMessage("السلة فارغة حالياً. اختر خدمة لشحنها!");
            builder.setPositiveButton("حسناً", null);
            builder.show();
            return;
        }

        StringBuilder details = new StringBuilder();
        double total = 0;

        for (int i = 0; i < cart.size(); i++) {
            CartOrder order = cart.get(i);
            details.append(i + 1).append(". ").append(order.service.title).append("\n")
                   .append("   • الحساب/ID: ").append(order.userTargetInfo).append("\n")
                   .append("   • السعر: $").append(order.service.price).append("\n\n");
            total += order.service.price;
        }

        details.append("━━━━━━━━━━━━━━━\n");
        details.append("الإجمالي المطلوب: $").append(String.format("%.2f", total));

        builder.setMessage(details.toString());

        builder.setPositiveButton("تأكيد ودفع الآن", (dialog, which) -> {
            cart.clear();
            updateCartBadge();
            Toast.makeText(MainActivity.this, "تم إرسال طلبك بنجاح! جاري تنفيذ الشحن والتفعيل.", Toast.LENGTH_LONG).show();
        });

        builder.setNeutralButton("تفريغ السلة", (dialog, which) -> {
            cart.clear();
            updateCartBadge();
            Toast.makeText(MainActivity.this, "تم تفريغ السلة", Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("إغلاق", null);
        builder.show();
    }
}